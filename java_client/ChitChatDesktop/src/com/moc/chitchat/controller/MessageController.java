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
import java.io.IOException;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * MessageController provides the actions involved with messaging.
 */
@Component
public class MessageController {

    private HttpClient httpClient;
    private Configuration configuration;
    private MessageValidator messageValidator;
    private MessageResolver messageResolver;
    private ChitChatData chitChatData;
    private UserResolver userResolver;
    private CryptoFunctions cryptoFunctions;

    @Autowired
    MessageController(
            HttpClient httpClient,
            Configuration configuration,
            MessageValidator messageValidator,
            MessageResolver messageResolver,
            ChitChatData chitChatData,
            UserResolver userResolver,
            CryptoFunctions cryptoFunctions
    ) {
        this.httpClient = httpClient;
        this.configuration = configuration;
        this.messageValidator = messageValidator;
        this.messageResolver = messageResolver;
        this.chitChatData = chitChatData;
        this.userResolver = userResolver;
        this.cryptoFunctions = cryptoFunctions;
    }

    /**
     * send function will send a message from the logged in user to the required user using the parameters below.
     *
     * @param to      - The user that would receive a message
     * @param message - The actual message
     * @return - a new message object
     * @throws ValidationException         - if invalid recipient or message (e.g. empty) entered
     * @throws UnexpectedResponseException - unexpected response
     */
    public Message send(UserModel to, String message)
            throws Exception {

        String message_encrypt = cryptoFunctions.encrypt(message, to.getPublicKey());
        Message newMessage = this.messageResolver.createMessage(this.configuration.getLoggedInUser(), to,message, message_encrypt);
        Response response = httpClient.post("/api/v1/messages", newMessage);

        if (response.code() == 422) {
            this.messageValidator.throwErrorsFromResponse(response);
        } else if (response.code() != 201) {
            throw new UnexpectedResponseException(response);
        }

        this.chitChatData.addMessageToConversation(to, newMessage);

        return newMessage;

    }

    /**
     * Deals with receiving messages from other users.
     * @param receivedMessage - the message
     * @param username - the sender of the message
     * @return - a new message object
     */
    public Message receive(String receivedMessage, String username) throws Exception {
        UserModel from = this.userResolver.createUser(username);
        System.out.println(receivedMessage);

        String message_decrypt = cryptoFunctions.decrypt(receivedMessage,this.configuration.getLoggedInUser().getPrivatekey());
        Message message = this.messageResolver.createMessage(
            from,
            this.configuration.getLoggedInUser(),
            message_decrypt,
                receivedMessage
        );

        chitChatData.addMessageToConversation(from, message);

        return message;
    }
}
