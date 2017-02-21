package com.moc.chitchat.model;

import java.util.List;

/**
 * Conversation class shows the list of messages between users
 */
public class Conversation {

    private List<Message> messageList;

    public Conversation(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
