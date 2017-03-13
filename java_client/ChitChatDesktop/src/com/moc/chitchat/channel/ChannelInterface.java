package com.moc.chitchat.channel;

import org.json.JSONObject;

/**
 * ChannelInterface provides a skeleton for channels.
 */
public interface ChannelInterface {

    /**
     * getJoin returns the join JSONObject. The ref is handled by the Socket.
     * @return The message to join a channel.
     */
    JSONObject getJoin();

    /**
     * handleMessage handles a new message on this channel.
     * @param payload the payload sent to the channel.
     */
    void handleMessage(JSONObject payload) throws Exception;

    /**
     * getEvent returns the event this handler is for.
     * @return the event string this handler is for.
     */
    String getEvent();
}
