package com.moc.chitchat.application;

import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.util.ArrayList;

import javax.inject.Inject;


/**
 * ChitChatData provides the main application states.
 */
public class ChitChatMessagesConfiguration {


    /**
     * To listen on new messages.
     */
    public interface MessageConfigurationListener {
        void onNewMessageReceived();
    }

    private MessageConfigurationListener messageConfigurationListener;

    /**
     * ArrayList of Conversations. This allows the views to update automatically when this reference changes.
     */
    private ArrayList<ConversationModel> conversations;

    /**
     * Constructor for ChitChatData.
     */
    public ChitChatMessagesConfiguration() {
        this.conversations = new ArrayList<ConversationModel>();
        this.messageConfigurationListener = null;
    }

    /**
     * Sets the listener from the caller function, probably activities.
     * @param listener the listener object to set
     */
    public void setMessageConfigurationListener(MessageConfigurationListener listener) {
        this.messageConfigurationListener = listener;
    }

    /**
     * Returns the conversation for a given User. Creates a blank new one if it does not exist.
     *
     * @param user the user to create a conversation for.
     * @return the conversation associated with the given user.
     */
    public ConversationModel getConversation(UserModel user) {
        ConversationModel conversation = this.findConversation(user);

        if (conversation == null) {
            conversation = new ConversationModel(user);
            System.out.println(String.format("Created new conversation for: %s", user));
            this.conversations.add(conversation);
        }

        return conversation;
    }

    /**
     * Adds the message to the conversation with the receiver.
     *
     * @param user    - reciever of the message
     * @param message - message object
     */
    public void addMessageToConversation(UserModel user, MessageModel message, boolean isLocal) {
        ConversationModel conversation = getConversation(user);
        conversation.addMessage(message);
        if(!isLocal && this.messageConfigurationListener != null) {
            this.messageConfigurationListener.onNewMessageReceived();
        }
    }

    /**
     * Returns the conversations.
     *
     * @return the conversations.
     */
    public ArrayList<ConversationModel> getConversations() {
        return this.conversations;
    }

    /**
     * Finds a conversation for a given User.
     *
     * @param user the user to find a conversation for.
     * @return returns the conversation if found, null otherwise.
     */
    private ConversationModel findConversation(UserModel user) {
        for (ConversationModel conv : this.conversations) {
            if (conv.getOtherParticipant().getUsername().equals(user.getUsername())) {
                return conv;
            }
        }

        return null;
    }

}
