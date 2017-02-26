package com.moc.chitchat.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Conversation class shows the list of messages between users.
 */
public class Conversation implements Observable {

    private UserModel otherParticipant;

    private ObservableList<Message> messages;

    public Conversation(UserModel otherParticipant) {
        this.messages = FXCollections.observableArrayList();
        this.otherParticipant = otherParticipant;
    }

    public UserModel getOtherParticipant() {
        return this.otherParticipant;
    }

    /**
     * addMessage adds the message object to the Conversation between the two users.
     * @param message - The message object that will be added.
     * @return - returns the conversation
     */
    public Conversation addMessage(Message message) {
        this.messages.add(message);

        return this;
    }

    public ObservableList<Message> getMessages() {
        return this.messages;
    }

    public String toString() {
        return this.otherParticipant.getUsername();
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {

    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {

    }
}
