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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UserSearchControllerTest provides the tests for UserSearchController
 */
public class UserSearchControllerTest {

    @Test
    public void testSuccessfulSearch() throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();
        String pubkey= "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAhWx9x7ThkaLaCom5HZ3EyzXMZNg87AhDrxm2Z14FEGaiCgslMbxuxd5uYRwR2WToGHm1BcR8UeweKd/nYzOYRxA4kBU7zPuTRVBsik6xDevDBInd6Pf2eTwsVvkqQ1jY0zBbmbxuR60Xu4eQmudXOfqnG1kUBz6nqjcSHEd/GKtUKwxTQ2S/Ow1YTnkag4BANsheb7HyI3VfronQjhqwspzVbuyVi97Vu2pBO7wdsJVBzjIc7sxWOhPRr9V09f3erWJUiQxjY2Er8x48tvgQRmsar23ZqHIbt5T6tKGQU3dSLjHA+1HNjfd2JX7NPNabczQymg6DX9BfwcoJKVYJdnq4TvbHQckVPWZVu2IBM/VusePjRw9PCnaQ2C1MH/WBC7act6earIJne2cPHpCo+GaNuPLytzMFU6J4+npmU2NaXOAb5wjzJErg/c6HZIkfWHTWCp7RQpkfMC9XqsijOx9DDNs3CK0PCUo/7Gav8LiTPXDPPZp/D8+048PSAiYqt7kKTz3grN1rT4BiE/4ZsjReseEzpIXWskB37V+aTRfFJ1NX0qJGYhPYqJQiWTcBX5LsB60k+nrpSzkP6EAM2JoIEM2JlOFZ8GOJ96wUTTnH8SsnWQdfyMPox0mCxxM7RkaPM/OhpZv0FjWACWOZaq4qLywh+K2Zy7ytfW6L2m0CAwEAAQ==";
        // Not used, but for completeness
        String user = "John";
        String jsonResponse = "{" +
                "\"data\": " +
                String.format("[{\"username\": \"%s\", \"public_key\": \"%s\"}]", user, pubkey) +
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
        assertEquals(String.format("/api/v1/users?username=%s", user), recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }

    /**
     *  Test that not enough characters has been inputted.
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testValidator() throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
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
        assertEquals(String.format("/api/v1/users?username=%s", user), recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }

    @Test
    public void testUnexpectedResponse() throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
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
        assertEquals(String.format("/api/v1/users?username=%s", user), recordedRequest.getPath());
        assertEquals("GET", recordedRequest.getMethod());
        server.shutdown();
    }
}
