package com.moc.chitchat.resolver;

import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import org.springframework.stereotype.Component;

/**
 * MessageResolver provides the the methods involved with converting parameters into a Messaging object.
 */
@Component
public class MessageResolver {

    /**
     *  createMessage returns a new message with with the given parameters below.
     * @param from - This is the user that is sending the message
     * @param to - This is the user that is receiving messages
     * @param message - This is actual message
     * @return - The message object
     */
    public Message createMessage(UserModel from, UserModel to, String message) {

        return new Message(from, to, message);
    }
}
