package com.moc.chitchat.controller;

import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.application.ChitChatDataTest;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.crypto.CryptoFunctions;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.MessageResolver;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.MessageValidator;
import com.moc.chitchat.validator.UserValidator;
import javafx.embed.swing.JFXPanel;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.security.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * MessageControllerTest provides the tests for MessageController
 */
public class MessageControllerTest {

    @Test
    public void testRecipientAndMessageErrorMessage () throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"recipient\": [\"can't be blank\"]," +
                "\"message\": [\"can't be blank\"]" +
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
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver mockUserResolver = mock(UserResolver.class);

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        to.setPublicKey(userKeyPair.getPublic());
        String testMessage = "this is the message you should NOT see";
        String expectedMessage = "";
        String encrypTestMessage = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        Message message = new Message(from, to, expectedMessage, encrypTestMessage);


        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                mockUserResolver,
                cryptoFunctions
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

            fail();
        } catch (ValidationException v) {
            assertEquals(
                    "can't be blank",
                    v.getErrors()
                            .getFieldError("recipient")
                            .getDefaultMessage()
            );
            assertEquals(
                    "can't be blank",
                    v.getErrors()
                            .getFieldError("message")
                            .getDefaultMessage()
            );
        } catch (UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/messages", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedMessage, message.getMessage());
        server.shutdown();
    }

    @Test
    public void testRecipientErrorMessage () throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"recipient\": [\"can't be blank\"]," +
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
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver mockUserResolver = mock(UserResolver.class);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        to.setPublicKey(userKeyPair.getPublic());
        String testMessage = "this is the message you should NOT see";
        String expectedMessage = "";
        String encrypTestMessage = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        Message message = new Message(from, to, expectedMessage, encrypTestMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                mockUserResolver,
                cryptoFunctions
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

            fail();
        } catch (ValidationException v) {
            assertEquals(
                    "can't be blank",
                    v.getErrors()
                            .getFieldError("recipient")
                            .getDefaultMessage()
            );
        } catch (UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/messages", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedMessage, message.getMessage());
        server.shutdown();
    }

    @Test
    public void testMessageErrorMessage () throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"errors\": {" +
                "\"message\": [\"can't be blank\"]," +
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
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver mockUserResolver = mock(UserResolver.class);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        to.setPublicKey(userKeyPair.getPublic());
        String testMessage = "this is the message you should NOT see";
        String expectedMessage = "";
        String encrypTestMessage = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        Message message = new Message(from, to, expectedMessage, encrypTestMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                mockUserResolver,
                cryptoFunctions
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

            fail();
        } catch (ValidationException v) {
            assertEquals(
                    "can't be blank",
                    v.getErrors()
                            .getFieldError("message")
                            .getDefaultMessage()
            );
        } catch (UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/messages", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedMessage, message.getMessage());
        server.shutdown();
    }

    @Test
    public void testUnexpectedResponseMessage () throws Exception {
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
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver mockUserResolver = mock(UserResolver.class);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        to.setPublicKey(userKeyPair.getPublic());
        String testMessage = "this is the message you should NOT see";
        String encrypTestMessage = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        String expectedMessage = "";
        Message message = new Message(from, to, expectedMessage, encrypTestMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                mockUserResolver,
                cryptoFunctions
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

        } catch (ValidationException v) {
            fail();
            v.printStackTrace();
        } catch (UnexpectedResponseException e) {
            assertEquals(500, e.getResponse().code());
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/messages", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedMessage, message.getMessage());
        server.shutdown();
    }

    @Test
    public void testSuccessfulReceive () throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{\"data\":" +
        "[{\"public_key\":\"MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApUYOLj82dD0zybk/0fxF7fYxr12VOr6bv0xNOZXYNaPDSYEqe3VNAeOa5nMgatx8rJ5VGC/A/3xKBq243VDSBVcihvNIQT/MWDyRMhMG+PyEHLQaT3SiyC12ukWxFPQUpUwGvYIvEd1RE/nlYzt1NqOfOsWUtLd6Lx9QwxMVf8d25KSVenkQm6p9LVyk/kWWOjMleg7O4beKm4VbtxPu6qSHSYD5WX8yIzDlrFTzoOnM2X9bN4FgjAzMp4lsUtwwCozXewlb64ds/WQS/0dgWCnnzEWA2AAsuzAmjzdnGKc+Iz7vi5JsRKbcAIBb0OFruGUBYbRXxaICnuPaWCCLzxNRiu2Q+3wy9Nyr1PUXw7+As3CQiTCXlmTG1/SqlrYfxQPeWnmm4ujlme6HzLTzvjIM0CRZcw/1u7+/Tzu1xdpoE3gxR7s6TBJa8gxuewKqbbil6M7uGAU28uOQ3R7dsTKrGixVdqt6fsE6gQk4sGZesuPyDKmdZOZN9HndtnMzVTCrA3PAPyYaj5a3XLL/f+9n7Knux2m9YiIzCUBCrPLGePX0cDfnxgS16sj2k7GYIZx9jnorRLDFTHgWIKli2KBRU8vLraj5y6sOcUqTVFFbqotddEywt547TAGTT5DQqCl7X81ybf7oqahhyzV3BLFYkfGeqOGurVNwSltWQqMCAwEAAQ==\", "+
        "\"username\":\"john\"}]}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(200)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        String name = "John";
        String expectedMessage = "What'supJohn!!!!!!!!!!";

        // Set up controller
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver userResolver = new UserResolver();
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        // set up public/private keys
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        UserModel userModel = new UserModel(name);
        userModel.setPublicKey(userKeyPair.getPublic());
        userModel.setPrivatekey(userKeyPair.getPrivate());
        String encrypted_message = cryptoFunctions.encrypt(expectedMessage, userKeyPair.getPublic());
        when(mockConfiguration.getLoggedInUser()).thenReturn(userModel);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                userResolver,
                cryptoFunctions
        );

        Message foundMessage = new Message(userModel, "test");

        try {
            foundMessage = messageController.receive(encrypted_message, name);
        } catch(Exception e) {
            e.printStackTrace();
            fail("Failed");
        }

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/users?username=John", recordedRequest.getPath());
        assertEquals(expectedMessage, foundMessage.getMessage());

    }

    @Test
    public void testUnexpectedReceive () throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{\"data\":" +
                "[{\"public_key\":\"MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEApUYOLj82dD0zybk/0fxF7fYxr12VOr6bv0xNOZXYNaPDSYEqe3VNAeOa5nMgatx8rJ5VGC/A/3xKBq243VDSBVcihvNIQT/MWDyRMhMG+PyEHLQaT3SiyC12ukWxFPQUpUwGvYIvEd1RE/nlYzt1NqOfOsWUtLd6Lx9QwxMVf8d25KSVenkQm6p9LVyk/kWWOjMleg7O4beKm4VbtxPu6qSHSYD5WX8yIzDlrFTzoOnM2X9bN4FgjAzMp4lsUtwwCozXewlb64ds/WQS/0dgWCnnzEWA2AAsuzAmjzdnGKc+Iz7vi5JsRKbcAIBb0OFruGUBYbRXxaICnuPaWCCLzxNRiu2Q+3wy9Nyr1PUXw7+As3CQiTCXlmTG1/SqlrYfxQPeWnmm4ujlme6HzLTzvjIM0CRZcw/1u7+/Tzu1xdpoE3gxR7s6TBJa8gxuewKqbbil6M7uGAU28uOQ3R7dsTKrGixVdqt6fsE6gQk4sGZesuPyDKmdZOZN9HndtnMzVTCrA3PAPyYaj5a3XLL/f+9n7Knux2m9YiIzCUBCrPLGePX0cDfnxgS16sj2k7GYIZx9jnorRLDFTHgWIKli2KBRU8vLraj5y6sOcUqTVFFbqotddEywt547TAGTT5DQqCl7X81ybf7oqahhyzV3BLFYkfGeqOGurVNwSltWQqMCAwEAAQ==\", "+
                "\"username\":\"john\"}]}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .setBody(jsonResponse)
                .setResponseCode(500)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        String name = "John";
        String testMessage = "What'supJohn!!!!!!!!!!";

        // Set up controller
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver userResolver = new UserResolver();
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        // set up public/private keys
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        UserModel userModel = new UserModel(name);
        userModel.setPublicKey(userKeyPair.getPublic());
        userModel.setPrivatekey(userKeyPair.getPrivate());
        String encrypted_message = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        when(mockConfiguration.getLoggedInUser()).thenReturn(userModel);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                userResolver,
                cryptoFunctions
        );

        String expectedMessage = "we should see this message";
        Message foundMessage = new Message(userModel, expectedMessage);

        try {
            foundMessage = messageController.receive(encrypted_message, name);
            fail("Failed - expected an unexpected response");
        } catch(Exception e) {
            e.printStackTrace();
        }

        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("/api/v1/users?username=John", recordedRequest.getPath());
        assertEquals(expectedMessage, foundMessage.getMessage());
    }

    @Test
    public void testReceivedExistingConvo () throws Exception {
        String expectedMessage = "What'sup John!!!!!!!!!!";

        // Set up controller
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData chitChatData = new ChitChatData();
        UserResolver userResolver = new UserResolver();
        CryptoFunctions cryptoFunctions = new CryptoFunctions();

        // set up public/private keys
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        UserModel sender = new UserModel("Philip");
        UserModel userModel = new UserModel("John");
        userModel.setPublicKey(userKeyPair.getPublic());
        userModel.setPrivatekey(userKeyPair.getPrivate());
        String encrypted_message = cryptoFunctions.encrypt(expectedMessage, userKeyPair.getPublic());
        when(mockConfiguration.getLoggedInUser()).thenReturn(userModel);

        Message philipMessage = new Message(sender, "This is the message");
        Message philipMessage2 = new Message(sender, "This is another message");

        // Credit: https://rterp.wordpress.com/2015/04/04/javafx-toolkit-not-initialized-solved/
        new JFXPanel();

        chitChatData.addMessageToConversation(sender, philipMessage);
        chitChatData.addMessageToConversation(sender, philipMessage2);

        ChitChatDataTest.waitForRunLater();

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                chitChatData,
                userResolver,
                cryptoFunctions
        );

        Message foundMessage = new Message(userModel, "test message");

        try {
            foundMessage = messageController.receive(encrypted_message, "Philip");
            // Credit: https://rterp.wordpress.com/2015/04/04/javafx-toolkit-not-initialized-solved/
            new JFXPanel();
            ChitChatDataTest.waitForRunLater();
        } catch(Exception e) {
            e.printStackTrace();
            fail("Failed");
        }

        assertEquals(3, chitChatData.getConversation(sender).getMessages().size());
        assertEquals(expectedMessage, foundMessage.getMessage());
    }

    @Test
    public void testSuccessfulMessageSent() throws Exception {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"data\": {" +
                "\"sender\": \"Chief\"," +
                "\"recipient\": \"Fred\"," +
                "\"message\": \"this is the message you should see\"," +
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
        MessageResolver messageResolver = new MessageResolver();
        MessageValidator messageValidator = new MessageValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData mockChitChatData = mock(ChitChatData.class);
        UserResolver userResolver = new UserResolver();

        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        to.setPublicKey(userKeyPair.getPublic());
        String testMessage = "you should not see this message";
        String expectedMessage = "this is the message you should see";
        String encrypTestMessage = cryptoFunctions.encrypt(testMessage, userKeyPair.getPublic());
        Message message = new Message(from, to, testMessage, encrypTestMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                messageValidator,
                messageResolver,
                mockChitChatData,
                userResolver,
                cryptoFunctions
        );

        try {
            message = messageController.send(
                    to,
                    expectedMessage
            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = server.takeRequest();
        assertEquals("//api/v1/messages", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals(expectedMessage, message.getMessage());
        server.shutdown();

    }

}

