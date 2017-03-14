package com.moc.chitchat.channel.handler;

import org.json.JSONObject;

/**
 * HandlerInterface provides a skeleton for handlers.
 */
public interface HandlerInterface {

    /**
     * Returns the event that this handles.
     * @return String the event this handler handles.
     */
    String getEvent();

    /**
     * Processes the payload attached to the event.
     * @param payload The payload attached to the event.
     */
    void handleMessage(JSONObject payload);
}
