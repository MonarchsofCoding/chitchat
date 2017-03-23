package com.moc.chitchat.resolver;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * ConversationTest provides the test for the Conversation
 */
public class ConversationTest {

    @Test
    public void testConstructor() {
        UserModel receiver = new UserModel("bobby");
        Conversation conversation = new Conversation(receiver);

        assertEquals(receiver.getUsername(), conversation.getOtherParticipant().getUsername());
    }

    @Test
    public void testMessages() {
        UserModel receiver = new UserModel("Sophie");
        Conversation conversation = new Conversation(receiver);

        Message firstMessage = new Message(receiver, "woohoo!!");
        Message secondMessage = new Message(receiver, "Yup!!");
        conversation.addMessage(firstMessage);
        conversation.addMessage(secondMessage);

        ObservableList<Message> expectedMessages = FXCollections.observableArrayList();
        expectedMessages.add(firstMessage);
        expectedMessages.add(secondMessage);

        assertEquals(expectedMessages, conversation.getMessages());
    }

    @Test
    public void testToString() {
        UserModel receiver = new UserModel("bobby");
        Conversation conversation = new Conversation(receiver);

        assertEquals(receiver.getUsername(), conversation.toString());
    }
}
