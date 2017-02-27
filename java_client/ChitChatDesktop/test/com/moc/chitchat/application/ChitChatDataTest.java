package com.moc.chitchat.application;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * ChitChatDataTest provides tests for the ChitChatData
 */
public class ChitChatDataTest {

    @Test
    public void testAddingNewConversation() {
        ChitChatData chitChatData = new ChitChatData();

        UserModel user = new UserModel("Frank");
        Conversation conversation = chitChatData.getConversation(user);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        assertEquals(messages.size(), conversation.getMessages().size());
    }

    @Test
    public void testGetOnGoingConversation() {
        ChitChatData chitChatData = new ChitChatData();

        UserModel user = new UserModel("Frank");
        Message message = new Message(user, "This is the message");

        chitChatData.addMessageToConversation(user, message);

        Conversation foundConversations = chitChatData.getConversation(user);

        assertEquals(message.getMessage(), foundConversations.getMessages().get(0).getMessage());
        assertEquals(message.getTo().getUsername(),
                foundConversations.getOtherParticipant().getUsername());
    }

    @Test
    public void testGetListOfConversations() {
        ChitChatData chitChatData = new ChitChatData();

        UserModel frank = new UserModel("Frank");
        UserModel conor = new UserModel("Conor");

        chitChatData.getConversation(frank);
        chitChatData.getConversation(conor);

        assertEquals(2, chitChatData.getConversations().size());
    }

    @Test
    public void testAddToConvo() {
        ChitChatData chitChatData = new ChitChatData();

        UserModel frank = new UserModel("Frank");
        Message frankMessage = new Message(frank, "This is the message");

        Message frankMessage2 = new Message(frank, "This is another message");

        chitChatData.addMessageToConversation(frank, frankMessage);
        chitChatData.addMessageToConversation(frank, frankMessage2);

        assertEquals(2, chitChatData.getConversation(frank).getMessages().size());
        assertEquals(frankMessage.getMessage(), chitChatData.getConversation(frank).getMessages().get(0).getMessage());
        assertEquals(frankMessage2.getMessage(), chitChatData.getConversation(frank).getMessages().get(1).getMessage());
    }
}
