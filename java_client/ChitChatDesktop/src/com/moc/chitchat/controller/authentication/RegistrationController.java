package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;

/**
 * RegistrationController provides the actions involved with registering a User.
 */
@Component
public class RegistrationController {

    private UserResolver userResolver;

    private UserValidator userValidator;

    private HttpClient httpClient;

    @Autowired
    RegistrationController(
        UserResolver userResolver,
        UserValidator validator,
        HttpClient httpClient
    ) {
        this.userResolver = userResolver;
        this.userValidator = validator;
        this.httpClient = httpClient;
    }

    public void registerUser(String username, String password, String passwordCheck) throws Exception {
        System.out.printf("Username: %s | Password: %s | Password Check: %s\n", username, password, passwordCheck);

        // Create the User object from parameters.
        UserModel user = this.userResolver.createUser(username, password, passwordCheck);
        //System.out.println("kodas2222");
        // Validate the User object. Throw exception if invalid.
        this.userValidator.validate(user);

       // Register the User object on the backend via a HTTP request.
        System.out.println("here");


        HttpResponse<JsonNode> response = this.httpClient.post("/api/v1/users", user);
        // Process HTTP response. Throw Exception if User invalid. Return void/true if Successful.
        if (response.getStatus() == 201) {

        } else if (response.getStatus() == 422) {

        } else {

            throw new Exception(String.format("HTTP/Server error %s", response.getStatus()));
        }

    }
}
