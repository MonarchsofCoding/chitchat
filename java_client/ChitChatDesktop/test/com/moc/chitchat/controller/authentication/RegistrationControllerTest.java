package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * RegistrationControllerTest provides tests for the RegistrationController
 */
public class RegistrationControllerTest {


    @Test
    public void testSuccessfulRegisterUser() throws IOException, InterruptedException, UnirestException {
        String validUsername = "alice";
        String validPassword = "abcde1234";
        String validPasswordCheck = "abcde1234";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"data\": {" +
                "\"username\": \"alice\"" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(201)
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
        UserModel userModel = new UserModel("testName");
        try {
            userModel = registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        assertEquals("alice", userModel.getUsername().toString());
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/users", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        server.shutdown();
    }

    @Test
    public void testUnexpectedResponse()
            throws UnirestException, IOException, InterruptedException {
        String validUsername = "alice";
        String validPassword = "abcde1234";
        String validPasswordCheck = "abcde1234";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"data\": {" +
                "\"username\": \"alice\"" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
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
        UserModel userModel = new UserModel("testName");
        try {
            userModel = registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );

        } catch (ValidationException v) {
            server.shutdown();
        } catch (UnexpectedResponseException e) {
            assertEquals("testName", userModel.getUsername().toString());
            server.shutdown();
        }

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/users", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());

        server.shutdown();
    }

    /**
     * This test that if the server responds with 'should be at least 8 character(s)'
     *
     * @throws UnirestException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testValidationExceptionPassword()
            throws UnirestException, IOException, InterruptedException {
        String validUsername = "alice";
        String validPassword = "12345";
        String validPasswordCheck = "12345";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"password\": [\"should be at least 8 character(s)\"]" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(422)
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
        UserModel userModel = new UserModel("testName");
        try {
            userModel = registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );

        } catch (ValidationException v) {
            v.printStackTrace();
            server.shutdown();
        } catch (UnexpectedResponseException e) {
            e.printStackTrace();
            server.shutdown();
        }

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/users", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("testName", userModel.getUsername().toString());
        server.shutdown();
    }

    /**
     * This tests the server error for a duplicated username
     *
     * @throws UnirestException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testValidationExceptionUsername()
            throws UnirestException, IOException, InterruptedException {
        String validUsername = "alice";
        String validPassword = "12345678";
        String validPasswordCheck = "12345678";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"username\": [\"has already been taken\"]" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(422)
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
        UserModel userModel = new UserModel("testName");
        try {
            userModel = registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );
        } catch (ValidationException v) {
            server.shutdown();
        } catch (UnexpectedResponseException e) {
            e.printStackTrace();
            server.shutdown();
        }
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/users", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("testName", userModel.getUsername().toString());
        server.shutdown();
    }
}