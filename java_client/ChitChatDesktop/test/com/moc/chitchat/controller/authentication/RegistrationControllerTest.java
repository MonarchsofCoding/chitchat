package com.moc.chitchat.controller.authentication;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.exception.ValidationException;
import org.junit.Test;


import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.springframework.validation.Errors;

/**
 * RegistrationControllerTest provides tests for the RestriagtionController
 */
public class RegistrationControllerTest {

    @Test
    public void testConstructor() {
        UserResolver userResolver = mock(UserResolver.class);
        UserValidator userValidator = mock(UserValidator.class);
        HttpClient httpClient = mock(HttpClient.class);

        RegistrationController register = new RegistrationController(userResolver, userValidator, httpClient);
        assertNotNull(register);
        assertEquals(register.getClass(), RegistrationController.class);
    }

}
