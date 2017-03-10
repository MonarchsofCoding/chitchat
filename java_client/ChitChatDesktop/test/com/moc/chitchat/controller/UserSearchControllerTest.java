package com.moc.chitchat.controller;

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
 * UserSearchControllerTest provides the tests for UserSearchController
 */
public class UserSearchControllerTest {

    @Test
    public void testSuccessfulSearch() throws IOException, InterruptedException {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String user = "John";
        String jsonResponse = "{" +
                "\"data\": " +
                "[{\"username\": \""+user+"\"}]" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(200)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        // Set up controller
        UserResolver userResolver = new UserResolver();
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);

        UserSearchController userSearchController = new UserSearchController(
                httpClient,
                userResolver,
                userValidator
        );

        try {
            userSearchController.searchUser(
                    user
            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/users?username="+user, recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }

    /**
     *  Test that not enough characters has been inputted.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testValidator() throws IOException, InterruptedException {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        // {"username":["should be at least 3 character(s)"]}
        String user = "Jo";
        String jsonResponse = "{" +
                "\"errors\": " +
                "{\"username\": [\"should be at least 3 character(s)\"]" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(400)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        // Set up controller
        UserResolver userResolver = new UserResolver();
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);

        UserSearchController userSearchController = new UserSearchController(
                httpClient,
                userResolver,
                userValidator
        );

        try {
            userSearchController.searchUser(
                    user
            );
        } catch (ValidationException v) {
            assertEquals("should be at least 3 character(s)", v.getErrors().getFieldError().getDefaultMessage());
        } catch (UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/users?username="+user, recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }

    @Test
    public void testUnexpectedResponse() throws IOException, InterruptedException {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        // {"username":["should be at least 3 character(s)"]}
        String user = "John";
        String jsonResponse = "{" +
                "\"error\": \"Unauthorized"+
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(401)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        // Set up controller
        UserResolver userResolver = new UserResolver();
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);

        UserSearchController userSearchController = new UserSearchController(
                httpClient,
                userResolver,
                userValidator
        );

        try {
            userSearchController.searchUser(
                    user
            );
        } catch (ValidationException v) {
            fail();
            v.printStackTrace();
        } catch (UnexpectedResponseException e) {
            e.printStackTrace();
            assertEquals(401, e.getResponse().code());
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/users?username="+user, recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }
}
