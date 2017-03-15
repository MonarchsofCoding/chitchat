package com.moc.chitchat.exception;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.controller.authentication.RegistrationController;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UnexpectedResponseExceptionTest provides Tests for the UnexpectedResponseException
 */
public class UnexpectedResponseExceptionTest {

    @Test
    public void testUnexpectedResponse()
            throws IOException, InterruptedException {
        String validUsername = "alice";
        String validPassword = "abcde1234";
        String validPasswordCheck = "abcde1234";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setResponseCode(500)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        // Set up controller
        UserResolver userResolver = new UserResolver();
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);

        RegistrationController registrationController = new RegistrationController(
                userResolver,
                userValidator,
                httpClient
        );

        try {
            registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );

        } catch (ValidationException v) {
            fail("Should not enter validation exception");
        } catch (UnexpectedResponseException e) {
            e.printStackTrace();
            assertEquals(500, e.getResponse().code());
            server.shutdown();
        }

        server.shutdown();
    }
}
