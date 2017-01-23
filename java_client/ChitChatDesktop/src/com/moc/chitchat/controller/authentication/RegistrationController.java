package com.moc.chitchat.controller.authentication;

import org.springframework.stereotype.Component;

/**
 * RegistrationController provides the actions involved with registering a User.
 */
@Component
public class RegistrationController {

    public void registerUser(String username, String password, String passwordCheck) {
        System.out.printf("Username: %s | Password: %s | Password Check: %s\n", username, password, passwordCheck);

        // Create the User object from parameters.

        // Validate the User object. Throw exception if invalid.

        // Register the User object on the backend via a HTTP request.

        // Process HTTP response. Throw Exception if User invalid. Return void/true if Successful.
    }
}
