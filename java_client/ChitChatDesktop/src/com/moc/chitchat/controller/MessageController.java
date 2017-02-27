package com.moc.chitchat.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.MessageResolver;
import com.moc.chitchat.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MessageController provides the actions involved with messaging.
 */
@Component
public class MessageController {

    private HttpClient httpClient;
    private Configuration configuration;
    private UserValidator userValidator;
    private MessageResolver messageResolver;
    private ChitChatData chitChatData;

    @Autowired
    MessageController(
            HttpClient httpClient,
            Configuration configuration,
            UserValidator userValidator,
            MessageResolver messageResolver,
            ChitChatData chitChatData
    ) {
        this.httpClient = httpClient;
        this.configuration = configuration;
        this.userValidator = userValidator;
        this.messageResolver = messageResolver;
        this.chitChatData = chitChatData;
    }

    /**
     * send function will send a message from the logged in user to the required user using the parameters below.
     *
     * @param to      - The user that would receive a message
     * @param message - The actual message
     * @return - a new message object
     * @throws UnirestException            - if invalid post with http client
     * @throws ValidationException         - if invalid recipient or message (e.g. empty) entered
     * @throws UnexpectedResponseException - unexpected response
     */
    public Message send(UserModel to, String message)
            throws UnirestException, ValidationException, UnexpectedResponseException {

        Message newMessage = this.messageResolver.createMessage(this.configuration.getLoggedInUser(), to, message);
        HttpResponse<JsonNode> response = httpClient.post("/api/v1/messages", newMessage);

        if (response.getStatus() == 422) {
            this.userValidator.throwErrorsFromResponse(response);
        } else if (response.getStatus() != 201) {
            throw new UnexpectedResponseException(response);
        }

        this.chitChatData.addMessageToConversation(to, newMessage);

        return newMessage;
    }
}
