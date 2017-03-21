package com.moc.chitchat.application;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;




/**
 * ChitChatData provides the main application states.
 */
@Component
public class ChitChatData implements Observer {

    /**
     * ObservableList of Conversations. This allows the views to update automatically when this reference changes.
     */
    private ObservableList<Conversation> conversations;

    /**
     * Constructor for ChitChatData.
     */
    public ChitChatData(
        Configuration configuration
    ) {
        this.conversations = FXCollections.observableArrayList();
        configuration.addObserver(this);
    }

    /**
     * Returns the conversation for a given User. Creates a blank new one if it does not exist.
     * @param user the user to create a conversation for.
     * @return the conversation associated with the given user.
     */
    public Conversation getConversation(UserModel user) {
        Conversation conversation = this.findConversation(user);

        if (conversation == null) {
            conversation = new Conversation(user);
            System.out.println(String.format("Created new conversation for: %s", user));
            this.conversations.add(conversation);
        }

        return conversation;
    }

    /**
     * Adds the message to the conversation with the receiver.
     * @param user - receiver of the message
     * @param message - message object
     */
    public void addMessageToConversation(UserModel user, Message message) {
        Platform.runLater(() -> {
            Conversation conversation = getConversation(user);
            conversation.addMessage(message);
        });
    }

    /**
     * Returns the conversations.
     * @return the conversations.
     */
    public ObservableList<Conversation> getConversations() {
        return this.conversations;
    }

    /**
     * Finds a conversation for a given User.
     * @param user the user to find a conversation for.
     * @return returns the conversation if found, null otherwise.
     */
    public Conversation findConversation(UserModel user) {
        for (Conversation c: this.conversations) {
            if (c.getOtherParticipant().getUsername().equals(user.getUsername())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void update(Observable observable, Object obj) {
        this.conversations.setAll();
    }
}
