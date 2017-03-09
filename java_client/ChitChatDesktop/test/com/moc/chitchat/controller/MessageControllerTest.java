package com.moc.chitchat.controller;

import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.MessageResolver;
import com.moc.chitchat.validator.UserValidator;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * MessageControllerTest provides the tests for MessageController
 */
public class MessageControllerTest {

    @InjectMocks
    MessageController mockMessageController;

    @Mock
    private Configuration configuration;

    @Mock
    private MessageResolver mockmessageResolver;

    @Mock
    private ChitChatData mockChitChatData;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSuccessfulMessageSent() throws IOException, InterruptedException {
        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // {"data":{"sender":"bob","recipient":"john","message":"hey"}}
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
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData chitChatData = mock(ChitChatData.class);

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        String testMessage = "you should not see this message";
        String expectedMessage = "this is the message you should see";
        Message message = new Message(from, to, testMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                userValidator,
                messageResolver,
                chitChatData
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
    public void testErrorMessage() throws IOException, InterruptedException {
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
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData chitChatData = mock(ChitChatData.class);

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        String expectedMessage = "";
        String testMessage = "this is the message you should NOT see";
        Message message = new Message(from, to, expectedMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                userValidator,
                messageResolver,
                chitChatData
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

        } catch (ValidationException v) {
            assertEquals("can't be blank", v.getErrors().getFieldError().getDefaultMessage());
        } catch(UnexpectedResponseException e) {
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
    public void testUnexpectedResponseMessage() throws IOException, InterruptedException {
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
        UserValidator userValidator = new UserValidator();
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);
        ChitChatData chitChatData = mock(ChitChatData.class);

        UserModel from = new UserModel("Chief");
        when(mockConfiguration.getLoggedInUser()).thenReturn(from);
        UserModel to = new UserModel("Fred");
        String expectedMessage = "";
        String testMessage = "this is the message you should NOT see";
        Message message = new Message(from, to, expectedMessage);

        MessageController messageController = new MessageController(
                httpClient,
                mockConfiguration,
                userValidator,
                messageResolver,
                chitChatData
        );

        try {
            message = messageController.send(
                    to,
                    testMessage
            );

        } catch (ValidationException v) {
            fail();
            v.printStackTrace();
        } catch(UnexpectedResponseException e) {
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
    public void testSuccessfulReceive() {
        String name = "John";
        String myText = "What's up John!!!!!!!!!!";

        mockMessageController.receive(myText, name);
    }

   /* @Mock
    private HttpClient httpClient;
    @Mock
    private Configuration configuration;
    @Mock
    private UserValidator userValidator;
    @Mock
    private MessageResolver messageResolver;
    @Mock
    private ChitChatData chitChatData;
    @Mock
    private HttpResponse<JsonNode> mockResponse;
    @Mock
    private Message message;

    @InjectMocks
    MessageController messageController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testConstructor() {
        assertNotNull(this.messageController);
        assertEquals(MessageController.class, this.messageController.getClass());
    }

    @Test
    public void testSendInvalidInfo()
            throws UnirestException, ValidationException, UnexpectedResponseException {
        UserModel from = new UserModel("Monty");
        UserModel to = new UserModel("Kerry");

        Message newMessage = this.messageResolver.createMessage(from, to, "");

        // Stub the HTTPClient to return the mocked response
        when(this.httpClient.post("/api/v1/messages", newMessage)).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(422);

        ValidationException mockValidationException = mock(ValidationException.class);
        when(mockValidationException.getMessage())
                .thenReturn("Validation Exception");

        doThrow(mockValidationException).when(this.userValidator).throwErrorsFromResponse(mockResponse);

        try {
            this.messageController.send(to, "message");
        } catch (ValidationException validationException) {
            assertEquals("Validation Exception", validationException.getMessage());
        }
    }

    @Test
    public void testUnexpectedException()
            throws UnirestException, ValidationException, UnexpectedResponseException {
        UserModel from = new UserModel("Monty");
        UserModel to = new UserModel("Kerry");

        Message newMessage = this.messageResolver.createMessage(from, to, "");

        // Stub the HTTPClient to return the mocked response
        when(this.httpClient.post("/api/v1/messages", newMessage)).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(500);

        try {
            this.messageController.send(to, "message");
        } catch (UnexpectedResponseException unexpectedResponseException) {
            assertEquals("Unexpected Response code: 500", unexpectedResponseException.getMessage());
        }
    }

    @Test
    public void testSuccessfulSend()
            throws UnirestException, ValidationException, UnexpectedResponseException {
        UserModel from = new UserModel("Monty");
        UserModel to = new UserModel("Kerry");

        Message expectedMessage = this.messageResolver.createMessage(from, to, "welp");

        // Stub the HTTPClient to return the mocked response
        when(this.httpClient.post("/api/v1/messages", expectedMessage)).thenReturn(mockResponse);
        when(mockResponse.getStatus()).thenReturn(201);

        Conversation conversation = mock(Conversation.class);
        when(this.chitChatData.getConversation(to)).thenReturn(conversation);
        when(conversation.addMessage(message)).thenReturn(conversation);

        this.messageController.send(to, "message");
    }
    */
}
