package com.moc.chitchat.application;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;


/**
 * ChitChatData provides the main application states.
 */
@Component
public class ChitChatData {

    /**
     * ObservableList of Conversations. This allows the views to update automatically when this reference changes.
     */
    private ObservableList<Conversation> conversations;

    /**
     * Constructor for ChitChatData.
     */
    public ChitChatData() {
        this.conversations = FXCollections.observableArrayList();
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
    private Conversation findConversation(UserModel user) {
        for (Conversation c: this.conversations) {
            if (c.getOtherParticipant().equals(user)) {
                return c;
            }
        }

        return null;
    }

}
