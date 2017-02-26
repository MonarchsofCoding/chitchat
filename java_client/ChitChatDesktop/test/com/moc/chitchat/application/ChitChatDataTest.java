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


        Conversation conversation = chitChatData.getConversation(user);
        conversation.addMessage(message);

        Conversation foundConversations = chitChatData.getConversation(user);

        assertEquals(conversation.getMessages(), foundConversations.getMessages());
        assertEquals(conversation.getOtherParticipant().getUsername(),
                foundConversations.getOtherParticipant().getUsername());
    }

    @Test
    public void testGetListOfConversations() {
        ChitChatData chitChatData = new ChitChatData();

        UserModel frank = new UserModel("Frank");
        Message frankMessage = new Message(frank, "This is the message");

        UserModel conor = new UserModel("Conor");
        Message conorMessage = new Message(conor, "This is another message");

        chitChatData.getConversation(frank);
        chitChatData.getConversation(conor);


        ObservableList<Message> messages = FXCollections.observableArrayList();
        assertEquals(2, chitChatData.getConversations().size());
    }
}
