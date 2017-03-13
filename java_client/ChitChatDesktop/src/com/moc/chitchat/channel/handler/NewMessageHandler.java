package com.moc.chitchat.channel.handler;

import com.moc.chitchat.controller.MessageController;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * NewMessageHandler handles the new:message event.
 */
@Component
public class NewMessageHandler implements HandlerInterface {

    private MessageController messageController;

    /**
     * Constructor for NewMessageHandler.
     * @param messageController the message controller.
     */
    @Autowired
    public NewMessageHandler(
        MessageController messageController
    ) {
        this.messageController = messageController;
    }

    /**
     *
     * @return
     */
    @Override
    public String getEvent() {
        return "new:message";
    }

    /**
     *
     * @param payload The payload attached to the event.
     */
    @Override
    public void handleMessage(JSONObject payload) {
        try {
            this.messageController.receive(
                    payload.getString("body"),
                    payload.getString("from")
            );
        } catch (Exception e) {
            // TODO: don't use global Exception!!!
            e.printStackTrace();
        }
    }
}
