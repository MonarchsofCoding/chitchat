package com.moc.chitchat.model;

import java.util.ArrayList;

/**
 * Conversation class shows the list of messages between users.
 */
public class ConversationModel {

    private UserModel otherParticipant;
    private ArrayList<MessageModel> messages;

    public ConversationModel(UserModel otherParticipant) {
        this.messages = new ArrayList<MessageModel>();
        this.otherParticipant = otherParticipant;
    }

    public UserModel getOtherParticipant() {
        return this.otherParticipant;
    }

    /**
     * addMessage adds the message object to the Conversation between the two users.
     *
     * @param message - The message object that will be added.
     * @return - returns the conversation
     */
    public ConversationModel addMessage(MessageModel message) {
        this.messages.add(message);

        return this;
    }

    public ArrayList<MessageModel> getMessages() {
        return this.messages;
    }

    public String toString() {
        return this.otherParticipant.getUsername();
    }

}
