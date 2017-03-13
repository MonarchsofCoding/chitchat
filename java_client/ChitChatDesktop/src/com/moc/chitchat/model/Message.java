package com.moc.chitchat.model;

import org.json.JSONObject;
import org.json.JSONString;

/**
 * Message class provides an individual message from a User to another User.
 */
public class Message implements JSONString {

    private UserModel from;
    private UserModel to;
    private String message;
    private String encrypted_message;

    /**
     * Message Constructor.
     * @param from - is the sender of a message
     * @param to - is the receiver of a message
     * @param message - the actual message
     */
    public Message(UserModel from, UserModel to, String message, String encrypted_message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.encrypted_message = encrypted_message;
    }

    public String getEncrypted_message() {
        return encrypted_message;
    }

    public void setEncrypted_message(String encrypted_message) {
        this.encrypted_message = encrypted_message;
    }

    public Message(UserModel to, String message) {
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

    /**
     * toJSONString returns the JSON representation of this Message.
     *
     * @return String JSON representation of the Message.
     */
    @Override
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject
                .put("recipient", this.to.getUsername())
                .put("message", this.encrypted_message)
        ;

        return jsonObject.toString();
    }

}
