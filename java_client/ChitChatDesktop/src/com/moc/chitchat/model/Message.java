package com.moc.chitchat.model;

/**
 * Message class provides an individual message from a User to another User
 */
public class Message {

    private UserModel from;
    private UserModel to;
    private String message;

    public Message(UserModel from, UserModel to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public void setFrom(UserModel from) {
        this.from = from;
    }

    public UserModel getFrom() {
        return from;
    }

    public UserModel getTo() {
        return to;
    }

    public void setTo(UserModel to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return String.format("%s: %s", this.from.getUsername(), this.message);
    }

}
