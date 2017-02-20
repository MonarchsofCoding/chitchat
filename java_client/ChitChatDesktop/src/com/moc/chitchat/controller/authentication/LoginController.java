package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * LoginController is the controller for login feature
 */
@Component
public class LoginController {
    private UserResolver userResolver;
    private HttpClient httpClient;
    private UserValidator userValidator;
    private Configuration configuration;
    @Autowired
    LoginController(
            UserResolver userResolver,
            HttpClient httpClient,
            UserValidator userValidator,
            Configuration configuration
    ) {
        this.userResolver = userResolver;
        this.httpClient = httpClient;
        this.userValidator = userValidator;
        this.configuration = configuration;
    }

    public UserModel loginUser(String username, String password) throws UnirestException, UnexpectedResponseException, ValidationException {
       // System.out.printf("Username: %s | Password length: %s | Password Check length: %s\n", username, password.length());

        // Create the User object from parameters.
        UserModel user = userResolver.createLoginUser(username,password);


        // Register the User object on the backend via a HTTP request.
        HttpResponse<JsonNode> response = this.httpClient.post("/api/v1/auth", user);

        // Process HTTP response. Throw Exception if User invalid. Return void/true if Successful.
        if (response.getStatus() == 401) {
            System.out.println(response.getStatus());

            this.userValidator.throwErrorsFromResponse(response);

        } else if (response.getStatus() != 200) {
            // Unexpected response code. e.g. 500

            throw new UnexpectedResponseException(response);
        }

        // Set the authorization token to the user
        user.setAuthToken(response.getBody().getObject().getJSONObject("data").get("authToken").toString());

        this.configuration.setLoggedInUser(user);
            //open chatroom
        return user;
    }

}
