package com.moc.chitchat.application;

/**
 * To store the information (recipient username and the messages) of the current chat.
 */

public class CurrentChatConfiguration {

    //Current recipient's username.

    String currentRecipientUsername = "";

    /* The messages between the current recipient */

    //TODO: Message model here

    public String getCurrentRecipientUsername() {
        return this.currentRecipientUsername;
    }

    public void setCurrentRecipientUsername(String newRecipient) {
        this.currentRecipientUsername = newRecipient;
    }

    public void cleanCurrentRecipientUsername() {
        this.currentRecipientUsername = "";
    }
}
