package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;

/**
 * Created by spiros on 09/02/17.
 */
@Component
public class LoginController {
    private UserResolver userResolver;
    private HttpClient httpClient;
    private UserValidator userValidator;
    @Autowired
    LoginController(
            UserResolver userResolver,
            HttpClient httpClient,
            UserValidator userValidator
    ) {
        this.userResolver = userResolver;
        this.httpClient = httpClient;
        this.userValidator = userValidator;
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
            //open chatroom
        return user;
    }

}
