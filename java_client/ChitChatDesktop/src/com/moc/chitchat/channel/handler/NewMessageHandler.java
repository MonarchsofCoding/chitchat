package com.moc.chitchat.channel.handler;

import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * NewMessageHandler handles the new:message event.
 */
@Component
public class NewMessageHandler implements HandlerInterface {

    private MessageController messageController;

    /**
     * Constructor for NewMessageHandler.
     * @param messageController the message controller.
     */
    @Autowired
    public NewMessageHandler(
        MessageController messageController
    ) {
        this.messageController = messageController;
    }

    /**
     *This function gets the new message.
     * @return string of the new message.
     */
    @Override
    public String getEvent() {
        return "new:message";
    }

    /**
     *This function extracts the sender and the message form the json object.
     * @param payload The payload attached to the event.
     */
    @Override
    public void handleMessage(JSONObject payload) {
        try {
            this.messageController.receive(
                    payload.getString("body"),
                    payload.getString("from")
            );
        } catch (
            BadPaddingException
            | NoSuchAlgorithmException
            | IOException
            | IllegalBlockSizeException
            | InvalidKeyException
            | NoSuchPaddingException
            | InvalidKeySpecException
            | UnexpectedResponseException exception
        ) {
            exception.printStackTrace();
        }
    }
}
