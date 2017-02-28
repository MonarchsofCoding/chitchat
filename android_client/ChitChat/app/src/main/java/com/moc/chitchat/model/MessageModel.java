package com.moc.chitchat.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Message class provides an individual message from a User to another User.
 */
public class MessageModel {

    private UserModel from;
    private UserModel to;
    private String message;

    /**
     * Message Constructor.
     *
     * @param from    - is the sender of a message
     * @param to      - is the receiver of a message
     * @param message - the actual message
     */
    public MessageModel(UserModel from, UserModel to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public MessageModel(UserModel to, String message) {
        this.to = to;
        this.message = message;
    }

    public UserModel getFrom() {
        return from;
    }

    public void setFrom(UserModel from) {
        this.from = from;
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
     * toJSONObject returns the JSON representation of this Message.
     *
     * @return String JSON representation of the Message.
     */
    public JSONObject tojsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject
            .put("recipient", this.to.getUsername())
            .put("message", this.message);

        return jsonObject;
    }

}
