package com.moc.chitchat.model;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * MessageTest provides the tests for the Message
 */
public class MessageTest {
/*
    @Test
    public void testConstrutor() {
        UserModel from = new UserModel("Shana");
        UserModel to = new UserModel("Vinny");
        String messageText = "I want to send you a message";

        Message message = new Message(from, to, messageText);

        assertEquals(from.getUsername(), message.getFrom().getUsername());
        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }

    @Test
    public void testSecondConstructor() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";

        Message message = new Message(to, messageText);

        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }

    @Test
    public void testSetTo() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";
        Message message = new Message(to, messageText);

        UserModel receiver = new UserModel("Phil");
        message.setTo(receiver);

        assertEquals(receiver.getUsername(), message.getTo().getUsername());
    }

    @Test
    public void testSetFrom() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        Message message = new Message(from, to, messageText);

        UserModel sender = new UserModel("Captain");

        message.setFrom(sender);

        assertEquals(sender.getUsername(), message.getFrom().getUsername());
    }

    @Test
    public void testSetMessage() {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";
        Message message = new Message(to, messageText);

        message.setMessage("Halo, the master chief collection!");

        assertEquals("Halo, the master chief collection!", message.getMessage());
    }

    @Test
    public void testToJSONString()
    {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";
        Message message = new Message(to, messageText);

        String expectedString = String.format("{\"recipient\":\"%s\",\"message\":\"%s\"}",
                to.getUsername(), messageText);
        assertEquals(expectedString, message.toJSONString());
    }

    @Test
    public void testToString() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        Message message = new Message(from, to, messageText);

        String expectedString = String.format("%s: %s", from.getUsername(), messageText);

        assertEquals(expectedString, message.toString());
    }*/
}
