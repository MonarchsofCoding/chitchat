package com.moc.chitchat.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ConversationModelTest {

    @Test
    public void testConstrutor() {
        UserModel otherUser = new UserModel("Shana");
        ConversationModel conv = new ConversationModel(otherUser);

        assertEquals(otherUser.getUsername(), conv.getOtherParticipant().getUsername());
        assertEquals(otherUser.getUsername(), conv.getOtherParticipant().getUsername());
    }

    @Test
    public void testGetOtherParticipant() {
        UserModel otherUser = new UserModel("David");
        ConversationModel conv = new ConversationModel(otherUser);

        assertEquals(otherUser.getUsername(), conv.getOtherParticipant().getUsername());
    }

    @Test
    public void testGetMessages() {
        UserModel otherUser = new UserModel("David");
        ConversationModel conv = new ConversationModel(otherUser);

        conv.getMessages();
    }

    @Test
    public void testAddMessage() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        MessageModel message = new MessageModel(from, to, messageText);

        ConversationModel conv = new ConversationModel(from);

        conv.addMessage(message);

        assertEquals(conv.getMessages().get(0).getMessage(), message.getMessage());
    }

    @Test
    public void testSetMessage() {
        UserModel otherUser = new UserModel("David");
        ConversationModel conv = new ConversationModel(otherUser);

        assertEquals(conv.toString(), otherUser.getUsername());
    }
}
