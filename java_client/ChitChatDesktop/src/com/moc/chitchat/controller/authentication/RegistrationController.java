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

    /**
     * Registering the user using the username, password and passwordCheck.
     * @param username - this provides the input field username
     * @param password - this provides the input field password
     * @param passwordCheck - this provides the input field passwordCheck
     * @throws ValidationException - If invalid username,password or passwordcheck
     * @throws UnirestException - If invalid post with the httpClient
     * @throws UnexpectedResponseException - Unexpected response
     */
    public void registerUser(String username, String password, String passwordCheck)
            throws ValidationException, UnirestException, UnexpectedResponseException {

        System.out.printf("Username: %s | Password length: %s | Password Check length: %s\n",
                username, password.length(), passwordCheck.length());

        // Create the User object from parameters.
        UserModel user = this.userResolver.createUser(username, password, passwordCheck);

        // Validate the User object. Throw exception if invalid.
        this.userValidator.validate(user);

        // Register the User object on the backend via a HTTP request.
        HttpResponse<JsonNode> response = this.httpClient.post("/api/v1/users", user);

        // Process HTTP response. Throw Exception if User invalid. Return void/true if Successful.
        if (response.getStatus() == 422) {
            // Validation failed or username taken.
            this.userValidator.throwErrorsFromResponse(response);
        } else if (response.getStatus() != 201) {
            // Unexpected response code. e.g. 500
            throw new UnexpectedResponseException(response);
        }
    }
}
