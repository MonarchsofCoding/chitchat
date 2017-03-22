package com.moc.chitchat.controller.authentication;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.client.WebSocketClient;
import com.moc.chitchat.crypto.CryptoFunctions;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import java.io.IOException;
import java.security.KeyPair;

import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * LoginController is the controller for login feature.
 */
@Component
public class LoginController {
    private UserResolver userResolver;
    private HttpClient httpClient;
    private UserValidator userValidator;
    private Configuration configuration;
    private WebSocketClient webSocketClient;
    private CryptoFunctions cryptoFunctions;

    @Autowired
    LoginController(
            UserResolver userResolver,
            HttpClient httpClient,
            UserValidator userValidator,
            Configuration configuration,
            WebSocketClient webSocketClient,
            CryptoFunctions cryptoFunctions
    ) {
        this.userResolver = userResolver;
        this.httpClient = httpClient;
        this.userValidator = userValidator;
        this.configuration = configuration;
        this.webSocketClient = webSocketClient;
        this.cryptoFunctions = cryptoFunctions;
    }

    /**
     * loginUser logs in the user using the parameters below.
     * @param username - name of the user
     * @param password - password for the user
     * @return - returns a new UserModel
     * @throws UnexpectedResponseException - unexpected response
     * @throws ValidationException - if incorrect username or password
     */
    public UserModel loginUser(String username, String password)
            throws ValidationException, UnexpectedResponseException, IOException {
        //Create the public and private keys

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        // Create the User object from parameters.
        UserModel user = userResolver.createUser(username, password,userKeyPair.getPublic(),userKeyPair.getPrivate());

        // Register the User object on the backend via a HTTP request.
        Response response = this.httpClient.post("/api/v1/auth", user);

        // Process HTTP response. Throw Exception if User invalid. Return void/true if Successful.
        if (response.code() == 401) {
            this.userValidator.throwErrorsFromResponse(response);

        } else if (response.code() != 200) {
            // Unexpected response code. e.g. 500
            throw new UnexpectedResponseException(response);
        }

        // Set the authorization token to the user
        String jsonData = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonData);
        user.setAuthToken(jsonObject.getJSONObject("data").get("authToken").toString());
        this.configuration.setLoggedInUser(user);

        webSocketClient.connectToUserMessage(user);
        //open chatroom
        response.body().close();
        return user;
    }

}
