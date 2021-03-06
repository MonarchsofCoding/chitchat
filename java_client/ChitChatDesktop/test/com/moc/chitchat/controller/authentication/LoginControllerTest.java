package com.moc.chitchat.controller.authentication;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.client.SocketListener;
import com.moc.chitchat.client.WebSocketClient;
import com.moc.chitchat.crypto.CryptoFunctions;
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
import org.mockito.Mockito;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 *  LoginControllerTest provides test for the LoginController
 */
public class LoginControllerTest {


    @Test
    public void testSuccessfulLogin() throws Exception {
        String validUsername = "alice";
        String validPassword = "abcde1234";

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair pair = cryptoFunctions.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();




        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        String authToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9";
        String jsonResponse = "{" +
                "\"data\": {" +
                "\"username\": \"alice\"," +
                String.format("\"authToken\": \"%s\",", authToken) +
                "}" +
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
        SocketListener socketListener = mock(SocketListener.class);
        WebSocketClient webSocketClient = new WebSocketClient(mockConfiguration, socketListener);

        LoginController loginController = new LoginController(
                userResolver,
                httpClient,
                userValidator,
                mockConfiguration,
                webSocketClient,
                cryptoFunctions
        );
        UserModel userModel = new UserModel("testName");
        try {
            userModel = loginController.loginUser(
                    validUsername,
                    validPassword

            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }
        UserModel userModel1 = new UserModel("testModel") ;

        userModel1 = userResolver.createUser("alice","abcde1234",publicKey,privateKey);

        // assert requests
        assertEquals(authToken, userModel.getAuthToken());
        assertEquals("alice", userModel.getUsername());
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/auth", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        server.shutdown();
    }

    @Test
    public void testIncorrectCredientials() throws IOException, Exception {
        String validUsername = "alice";
        String validPassword = "";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"username\": [\"is incorrect\"]," +
                "\"password\": [\"is incorrect\"]," +
                "}" +
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
        SocketListener socketListener = mock(SocketListener.class);
        WebSocketClient webSocketClient = new WebSocketClient(mockConfiguration, socketListener);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        LoginController loginController = new LoginController(
                userResolver,
                httpClient,
                userValidator,
                mockConfiguration,
                webSocketClient,
                cryptoFunctions
        );
        UserModel userModel = new UserModel("testName");
        try {
            userModel = loginController.loginUser(
                    validUsername,
                    validPassword
            );

        } catch (ValidationException e) {
            assertEquals("is incorrect", e.getErrors().getFieldError().getDefaultMessage());
            assertEquals("testName", userModel.getUsername());
        } catch (UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/auth", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        server.shutdown();
    }

    @Test
    public void testUnexpectedErrorsLogin() throws Exception {
        String validUsername = "alice";
        String validPassword = "";
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair pair = cryptoFunctions.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();



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
        SocketListener socketListener = mock(SocketListener.class);
        WebSocketClient webSocketClient = new WebSocketClient(mockConfiguration, socketListener);

        LoginController loginController = new LoginController(
                userResolver,
                httpClient,
                userValidator,
                mockConfiguration,
                webSocketClient,
                cryptoFunctions

        );
        UserModel userModel = new UserModel("testName");
        try {
            userModel = loginController.loginUser(
                    validUsername,
                    validPassword
            );

        } catch (ValidationException e) {
            fail();
            e.printStackTrace();
        } catch (UnexpectedResponseException e) {
            assertEquals("testName", userModel.getUsername());
        }
        UserModel userModel1 = new UserModel("testModel") ;

        userModel1 = userResolver.createUser(validUsername,validPassword,publicKey,privateKey);


        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/auth", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        server.shutdown();
    }

}