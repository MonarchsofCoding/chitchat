package com.moc.chitchat.controller;

import com.moc.chitchat.application.ChitChatData;
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

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void initMocks() throws Exception {
        MockitoAnnotations.initMocks(this);
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        generator.initialize(4096, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
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
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);
            when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
            HttpClient httpClient = new HttpClient(mockConfiguration);
            ChitChatData mockChitChatData = mock(ChitChatData.class);
            UserResolver userResolver = new UserResolver();

            UserModel from = new UserModel("Chief");
            when(mockConfiguration.getLoggedInUser()).thenReturn(from);
            UserModel to = new UserModel("Fred");
            String testMessage = "you should not see this message";
            String expectedMessage = "this is the message you should see";
            String decrypted_message = cryptoFunctions.decrypt(testMessage, privateKey);
            Message message = new Message(from, to, decrypted_message, testMessage);

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
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);
            when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
            HttpClient httpClient = new HttpClient(mockConfiguration);
            ChitChatData mockChitChatData = mock(ChitChatData.class);
            UserResolver mockUserResolver = mock(UserResolver.class);

            UserModel from = new UserModel("Chief");
            when(mockConfiguration.getLoggedInUser()).thenReturn(from);
            UserModel to = new UserModel("Fred");
            String expectedMessage = "";
            String testMessage = "this is the message you should NOT see";
            String decrypted_message = cryptoFunctions.decrypt(testMessage, privateKey);
            Message message = new Message(from, to, decrypted_message, testMessage);


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
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);

            UserModel from = new UserModel("Chief");
            when(mockConfiguration.getLoggedInUser()).thenReturn(from);
            UserModel to = new UserModel("Fred");
            String expectedMessage = "";
            String testMessage = "this is the message you should NOT see";
            String decrypted_message = cryptoFunctions.decrypt(testMessage, privateKey);
            Message message = new Message(from, to, decrypted_message, testMessage);

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
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);

            UserModel from = new UserModel("Chief");
            when(mockConfiguration.getLoggedInUser()).thenReturn(from);
            UserModel to = new UserModel("Fred");
            String expectedMessage = "";
            String testMessage = "this is the message you should NOT see";
            String decrypted_message = cryptoFunctions.decrypt(testMessage, privateKey);
            Message message = new Message(from, to, decrypted_message, testMessage);

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
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);

            UserModel from = new UserModel("Chief");
            when(mockConfiguration.getLoggedInUser()).thenReturn(from);
            UserModel to = new UserModel("Fred");
            String expectedMessage = "";
            String testMessage = "this is the message you should NOT see";
            String decrypted_message = cryptoFunctions.decrypt(testMessage, privateKey);
            Message message = new Message(from, to, decrypted_message, testMessage);

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
            String name = "John";
            String myText = "What's up John!!!!!!!!!!";

            HttpClient mockHttpClient = mock(HttpClient.class);
            Configuration mockConfiguration = mock(Configuration.class);
            MessageValidator mockMessageValidator = mock(MessageValidator.class);
            MessageResolver mockMessageResolver = mock(MessageResolver.class);
            ChitChatData mockChitChatData = mock(ChitChatData.class);
            UserResolver mockUserResolver = mock(UserResolver.class);
            CryptoFunctions cryptoFunctions = mock(CryptoFunctions.class);

            MessageController messageController = new MessageController(
                    mockHttpClient,
                    mockConfiguration,
                    mockMessageValidator,
                    mockMessageResolver,
                    mockChitChatData,
                    mockUserResolver,
                    cryptoFunctions
            );

            UserModel mockFromUser = mock(UserModel.class);
            when(mockUserResolver.createUser(name))
                    .thenReturn(mockFromUser)
            ;

            UserModel mockLoggedInUser = mock(UserModel.class);
            when(mockConfiguration.getLoggedInUser())
                    .thenReturn(mockLoggedInUser)
            ;
            String encrypted_message = cryptoFunctions.encrypt(myText, publicKey);

            Message mockMessage = mock(Message.class);
            when(mockMessageResolver.createMessage(mockFromUser, mockLoggedInUser, myText, encrypted_message))
                    .thenReturn(mockMessage)
            ;

            messageController.receive(myText, name);

            verify(mockChitChatData).addMessageToConversation(mockFromUser, mockMessage);
        }
    }

