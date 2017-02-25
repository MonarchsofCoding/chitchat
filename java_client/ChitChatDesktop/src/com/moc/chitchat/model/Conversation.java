package com.moc.chitchat.model;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Conversation class shows the list of messages between users
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

    public Conversation addMessage(Message m) {
        this.messages.add(m);

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
