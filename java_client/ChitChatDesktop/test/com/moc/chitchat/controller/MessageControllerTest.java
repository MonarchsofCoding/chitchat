package com.moc.chitchat.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.MessageResolver;
import com.moc.chitchat.validator.UserValidator;
import javafx.collections.ObservableList;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * MessageControllerTest provides the tests for MessageController
 */
public class MessageControllerTest {

    @Mock
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

    @Test
    public void testSuccessfulReceive() {
        String name = "John";
        String myText = "What's up John!!!!!!!!!!";

        this.messageController.receive(myText, name);
    }
}
