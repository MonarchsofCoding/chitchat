package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;

/**
 * To store the information (recipient username and the messages) of the current chat.
 */

public class CurrentChatConfiguration {

    //Current recipient.

    UserModel currentRecipient = null;

    public UserModel getCurrentRecipient() {
        return this.currentRecipient;
    }

    public void setCurrentRecipient(UserModel newRecipient) {
        this.currentRecipient = newRecipient;
    }

    public void cleanCurrentRecipient() {
        this.currentRecipient = null;
    }
}
