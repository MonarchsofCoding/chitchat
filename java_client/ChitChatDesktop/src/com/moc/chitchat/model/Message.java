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
    private String encryptedmessage;

    /**
     * Message Constructor.
     * @param from - is the sender of a message
     * @param to - is the receiver of a message
     * @param message - the actual message
     */
    public Message(UserModel from, UserModel to, String message, String encryptedmessage) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.encryptedmessage = encryptedmessage;
    }

    /**
     * This function gets the encrypted message.
     * @return String of encrypted message.
     */
    public String getEncrypted_message() {
        return encryptedmessage;
    }

    /**
     * This function sets the encrypted message.
     * @param encryptedmessage the String of encrypted message.
     */
    public void setEncrypted_message(String encryptedmessage) {
        this.encryptedmessage = encryptedmessage;
    }

    /**
     * This function sets to the message object the receiver and the actual message.
     * @param to the receiver.
     * @param message the message to the receiver.
     */
    public Message(UserModel to, String message) {
        this.to = to;

        this.message = message;
    }

    /**
     * This function sets the sender of the message.
     * @param from the sender.
     */
    public void setFrom(UserModel from) {
        this.from = from;
    }

    /**
     * This function gets the sender.
     * @return the sender.
     */
    public UserModel getFrom() {
        return from;
    }

    /**
     * Ths function gets the receiver.
     * @return the receiver.
     */
    public UserModel getTo() {
        return to;
    }

    /**
     * This function sets the receiver.
     * @param to the receiver.
     */
    public void setTo(UserModel to) {
        this.to = to;
    }

    /**
     * This function gets the message.
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * This function sets the message.
     * @param message the message.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * This function returns the string format.
     * @return the string format.
     */
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
                .put("message", this.encryptedmessage)
        ;

        return jsonObject.toString();
    }

}
