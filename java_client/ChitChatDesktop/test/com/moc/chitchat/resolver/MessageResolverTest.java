package com.moc.chitchat.resolver;

import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * MessageResolverTest provides the tests for the message resolver
 */
public class MessageResolverTest {

    @Test
    public void createMessageTest() {
        UserModel from = new UserModel("John");
        UserModel to = new UserModel("Vladimir");
        String messageText = "This is the message";

        MessageResolver messageResolver = new MessageResolver();
        Message message = messageResolver.createMessage(from, to, messageText);

        assertEquals(from.getUsername(), message.getFrom().getUsername());
        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }
}
