package com.moc.chitchat.application;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import org.junit.Test;


import java.util.Observable;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * ChitChatDataTest provides tests for the ChitChatData
 */
public class ChitChatDataTest {

    /**
     * Credit: http://stackoverflow.com/a/22846799
     * @throws InterruptedException
     */
    public static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(semaphore::release);
        semaphore.acquire();

    }

    @Test
    public void testAddingNewConversation() {
        Configuration mockConfiguration = mock(Configuration.class);
        ChitChatData chitChatData = new ChitChatData(mockConfiguration);

        UserModel user = new UserModel("Frank");
        Conversation conversation = chitChatData.getConversation(user);

        ObservableList<Message> messages = FXCollections.observableArrayList();
        assertEquals(messages.size(), conversation.getMessages().size());
    }

    @Test
    public void testGetOnGoingConversation() throws InterruptedException {
        Configuration mockConfiguration = mock(Configuration.class);
        ChitChatData chitChatData = new ChitChatData(mockConfiguration);

        UserModel user = new UserModel("Frank");
        Message message = new Message(user, "This is the message");

        // Credit: https://rterp.wordpress.com/2015/04/04/javafx-toolkit-not-initialized-solved/
        new JFXPanel();

        chitChatData.addMessageToConversation(user, message);

        ChitChatDataTest.waitForRunLater();

        Conversation foundConversations = chitChatData.getConversation(user);

        assertEquals(message.getMessage(), foundConversations.getMessages().get(0).getMessage());
        assertEquals(message.getTo().getUsername(),
                foundConversations.getOtherParticipant().getUsername());
    }

    @Test
    public void testGetListOfConversations() {
        Configuration mockConfiguration = mock(Configuration.class);
        ChitChatData chitChatData = new ChitChatData(mockConfiguration);

        UserModel frank = new UserModel("Frank");
        UserModel conor = new UserModel("Conor");

        chitChatData.getConversation(frank);
        chitChatData.getConversation(conor);

        assertEquals(2, chitChatData.getConversations().size());
    }

    @Test
    public void testAddToConvo() throws InterruptedException {
        Configuration mockConfiguration = mock(Configuration.class);
        ChitChatData chitChatData = new ChitChatData(mockConfiguration);

        UserModel frank = new UserModel("Frank");
        Message frankMessage = new Message(frank, "This is the message");

        Message frankMessage2 = new Message(frank, "This is another message");

        // Credit: https://rterp.wordpress.com/2015/04/04/javafx-toolkit-not-initialized-solved/
        new JFXPanel();

        chitChatData.addMessageToConversation(frank, frankMessage);
        chitChatData.addMessageToConversation(frank, frankMessage2);

        ChitChatDataTest.waitForRunLater();

        assertEquals(2, chitChatData.getConversation(frank).getMessages().size());
        assertEquals(frankMessage.getMessage(), chitChatData.getConversation(frank).getMessages().get(0).getMessage());
        assertEquals(frankMessage2.getMessage(), chitChatData.getConversation(frank).getMessages().get(1).getMessage());
    }

    @Test
    public void testConversationsAreRemovedWhenLoggedOut() {
        Configuration mockConfiguration = mock(Configuration.class);
        ChitChatData chitChatData = new ChitChatData(mockConfiguration);

        verify(mockConfiguration).addObserver(chitChatData);

        Observable mockObservable = mock(Observable.class);
        Object mockObject = mock(Object.class);

        // add a conversation for a user
        UserModel userModel = new UserModel("Ben");
        chitChatData.getConversation(userModel);
        assertEquals(1, chitChatData.getConversations().size());

        chitChatData.update(mockObservable, mockObject);

        assertEquals(0, chitChatData.getConversations().size());
    }
}
