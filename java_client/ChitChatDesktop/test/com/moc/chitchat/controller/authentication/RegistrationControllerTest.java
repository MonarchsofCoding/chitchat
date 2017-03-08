package com.moc.chitchat.controller.authentication;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
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

        try {
            registrationController.registerUser(
                    validUsername,
                    validPassword,
                    validPasswordCheck
            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/users", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        server.shutdown();
    }

/**
    @Test
    public void testLocalUnsuccessfulRegisterUser() throws ValidationException, UnirestException, UnexpectedResponseException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createUser(
                        "spiros",
                        "aaa",
                        "aaa"
                )
        ).thenReturn(mockUser);

        ValidationException mockValidationException = mock(ValidationException.class);
        when(mockValidationException.getMessage())
                .thenReturn("Validation Exception");

        doThrow(mockValidationException).when(this.mockUserValidator).validate(mockUser);


        // Run the function to test
        try {
            this.registrationController.registerUser(
                    "spiros",
                    "aaa",
                    "aaa"
            );
        } catch (ValidationException e) {
            assertEquals("Validation Exception", e.getMessage());
        }
    }

    @Test
    public void testServerUnsuccessfulRegisterUser() throws UnirestException, ValidationException, UnexpectedResponseException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createUser(
                        "spiros",
                        "aaa",
                        "aaa"
                )
        ).thenReturn(mockUser);


        // Create and define the mocked response to return 422 (unsuccessful)
        HttpResponse<JsonNode> mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus())
                .thenReturn(422);
        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.post("/api/v1/users", mockUser))
                .thenReturn(mockResponse);

        // Mock the ValidationException
        ValidationException mockValidationException = mock(ValidationException.class);
        when(mockValidationException.getMessage())
                .thenReturn("Validation Exception");

        // Run the function to test
        try {
            this.registrationController.registerUser(
                    "spiros",
                    "aaa",
                    "aaa"
            );
        } catch (ValidationException e) {
            assertEquals("Validation Exception", e.getMessage());
        }

        // Verify that UserValidator.validate was called
        verify(mockUserValidator).validate(mockUser);
        // Verify the UserValidator.throwErrorsFromResponse was called
        verify(mockUserValidator).throwErrorsFromResponse(mockResponse);
    }

    @Test
    public void testServerErrorRegisterUser() throws UnirestException, ValidationException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createUser(
                        "spiros",
                        "aaa",
                        "aaa"
                )
        ).thenReturn(mockUser);


        // Create and define the mocked response to return 422 (unsuccessful)
        HttpResponse<JsonNode> mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus())
                .thenReturn(500);
        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.post("/api/v1/users", mockUser))
                .thenReturn(mockResponse);

        // Run the function to test
        try {
            this.registrationController.registerUser(
                    "spiros",
                    "aaa",
                    "aaa"
            );
        } catch (UnexpectedResponseException e) {
            assertEquals("Unexpected Response code: 500", e.getMessage());
        }

        // Verify that UserValidator.validate was called
        verify(mockUserValidator).validate(mockUser);
    }
*/
}
