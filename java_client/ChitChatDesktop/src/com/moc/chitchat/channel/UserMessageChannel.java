package com.moc.chitchat.channel;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * UserMessageChannel provides a handler for the user messages channel.
 */
@Component
public class UserMessageChannel implements ChannelInterface {

    private Configuration configuration;
    private MessageController messageController;

    /**
     * UserMessageChannel constructor.
     * @param configuration the configuration object.
     * @param messageController the message controller.
     */
    public UserMessageChannel(
        Configuration configuration,
        MessageController messageController
    ) {
        this.configuration = configuration;
        this.messageController = messageController;
    }

    /**
     * getJoin returns the join JSONObject. The ref is handled by the Socket.
     * @return The message to join a channel.
     */
    @Override
    public JSONObject getJoin() {
        UserModel loggedInUser = this.configuration.getLoggedInUser();

        JSONObject payload = new JSONObject();
        payload.put("authToken", loggedInUser.getAuthToken());
        JSONObject joinChannel = new JSONObject();
        joinChannel.put("event", "phx_join");
        joinChannel.put("payload", payload);
        joinChannel.put("topic", String.format("user:%s", loggedInUser.getUsername()));

        return joinChannel;
    }

    /**
     * handleMessage handles a new message on this channel.
     * @param payload the payload sent to the channel.
     */
    @Override
    public void handleMessage(JSONObject payload) throws Exception {
        this.messageController.receive(
            payload.getString("body"),
            payload.getString("from")
        );
    }

    /**
     * getEvent returns the event this handler is for.
     * @return the event string this handler is for.
     */
    @Override
    public String getEvent() {
        return "new:message";
    }
}
