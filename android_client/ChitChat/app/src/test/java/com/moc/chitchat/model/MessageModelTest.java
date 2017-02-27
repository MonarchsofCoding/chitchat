package com.moc.chitchat.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * MessageModelTest provides the tests for the Message
 */
public class MessageModelTest {

    @Test
    public void testConstrutor() {
        UserModel from = new UserModel("Shana");
        UserModel to = new UserModel("Vinny");
        String messageText = "I want to send you a message";

        MessageModel message = new MessageModel(from, to, messageText);

        assertEquals(from.getUsername(), message.getFrom().getUsername());
        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }

    @Test
    public void testSecondConstructor() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";

        MessageModel message = new MessageModel(to, messageText);

        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }

    @Test
    public void testSetTo() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";
        MessageModel message = new MessageModel(to, messageText);

        UserModel receiver = new UserModel("Phil");
        message.setTo(receiver);

        assertEquals(receiver.getUsername(), message.getTo().getUsername());
    }

    @Test
    public void testSetFrom() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        MessageModel message = new MessageModel(from, to, messageText);

        UserModel sender = new UserModel("Captain");

        message.setFrom(sender);

        assertEquals(sender.getUsername(), message.getFrom().getUsername());
    }

    @Test
    public void testSetMessage() {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";
        MessageModel message = new MessageModel(to, messageText);

        message.setMessage("Halo, the master chief collection!");

        assertEquals("Halo, the master chief collection!", message.getMessage());
    }

    @Test
    public void testToJSONObject() throws JSONException {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";
        MessageModel message = new MessageModel(to, messageText);

        JSONObject expectedJSON = new JSONObject();

        expectedJSON
            .put("recipient", to.getUsername())
            .put("message", messageText);

        assertEquals(expectedJSON.toString(), message.tojsonObject().toString());
    }

    @Test
    public void testToString() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        MessageModel message = new MessageModel(from, to, messageText);

        String expectedString = String.format("%s: %s", from.getUsername(), messageText);

        assertEquals(expectedString, message.toString());
    }
}


