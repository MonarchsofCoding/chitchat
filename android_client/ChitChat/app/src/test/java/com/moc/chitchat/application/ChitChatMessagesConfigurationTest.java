package com.moc.chitchat.application;

import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ChitChatMessagesConfigurationTest {

    @Test
    public void testConstructor() {
        new ChitChatMessagesConfiguration();
    }

    @Test
    public void testAddingNewConversation() {
        ChitChatMessagesConfiguration chitChatData = new ChitChatMessagesConfiguration();

        UserModel user = new UserModel("Frank");
        ConversationModel conversation = chitChatData.getConversation(user);

        ArrayList<MessageModel> messages = new ArrayList<>();
        assertEquals(messages.size(), conversation.getMessages().size());
    }

    @Test
    public void testGetOnGoingConversation() {
        ChitChatMessagesConfiguration chitChatData = new ChitChatMessagesConfiguration();

        UserModel user = new UserModel("Frank");
        MessageModel message = new MessageModel(user, "This is the message");

        chitChatData.addMessageToConversation(user, message, false);

        ConversationModel foundConversations = chitChatData.getConversation(user);

        assertEquals(message.getMessage(), foundConversations.getMessages().get(0).getMessage());
        assertEquals(message.getTo().getUsername(),
            foundConversations.getOtherParticipant().getUsername());
    }

    @Test
    public void testClearChitChatMessagesConfiguration() {
        ChitChatMessagesConfiguration chitChatData = new ChitChatMessagesConfiguration();

        chitChatData.clearChitChatMessagesConfiguration();
    }

    @Test
    public void testGetListOfConversations() {
        ChitChatMessagesConfiguration chitChatData = new ChitChatMessagesConfiguration();

        UserModel frank = new UserModel("Frank");
        UserModel conor = new UserModel("Conor");

        chitChatData.getConversation(frank);
        chitChatData.getConversation(conor);

        assertEquals(2, chitChatData.getConversations().size());
    }

    @Test
    public void testAddToConvo() {
        ChitChatMessagesConfiguration chitChatData = new ChitChatMessagesConfiguration();

        ChitChatMessagesConfiguration.MessageConfigurationListener mockListener = mock(
            ChitChatMessagesConfiguration.MessageConfigurationListener.class
        );

        chitChatData.setMessageConfigurationListener(mockListener);

        UserModel frank = new UserModel("Frank");
        MessageModel frankMessage = new MessageModel(frank, "This is the message");

        MessageModel frankMessage2 = new MessageModel(frank, "This is another message");

        chitChatData.addMessageToConversation(frank, frankMessage, false);
        chitChatData.addMessageToConversation(frank, frankMessage2, false);

        assertEquals(2, chitChatData.getConversation(frank).getMessages().size());
        assertEquals(frankMessage.getMessage(), chitChatData.getConversation(frank).getMessages().get(0).getMessage());
        assertEquals(frankMessage2.getMessage(), chitChatData.getConversation(frank).getMessages().get(1).getMessage());
    }
}
