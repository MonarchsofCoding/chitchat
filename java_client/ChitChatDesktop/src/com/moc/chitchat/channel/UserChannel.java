package com.moc.chitchat.channel;


import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.channel.handler.HandlerInterface;
import com.moc.chitchat.channel.handler.NewMessageHandler;
import com.moc.chitchat.channel.handler.UserLogoutHandler;
import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * UserChannel provides the methods for channels on the user:* websocket topic.
 */
@Component
public class UserChannel implements ChannelInterface {

    private Configuration configuration;
    private Set<HandlerInterface> handlers;

    /**
     * UserMessageChannel constructor.
     * @param configuration the configuration object.
     * @param newMessageHandler handler for new messages.
     * @param userLogoutHandler  handler for user logout.
     */
    @Autowired
    public UserChannel(
            Configuration configuration,
            NewMessageHandler newMessageHandler,
            UserLogoutHandler userLogoutHandler
    ) {
        this.configuration = configuration;
        this.handlers = new HashSet<>();
        this.handlers.add(newMessageHandler);
        this.handlers.add(userLogoutHandler);
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
        joinChannel.put("topic", this.getTopic());

        return joinChannel;
    }

    /**
     * handleMessage handles a new message on this channel.
     * @param payload the payload sent to the channel.
     */
    @Override
    public void handleMessage(String event, JSONObject payload) throws Exception {

        for (HandlerInterface h: this.handlers) {
            if (event.equals(h.getEvent())) {
                h.handleMessage(payload);
            }
        }
    }

    /**
     * getTopic returns the topic for this channel.
     * @return String the topic this channels listens to.
     */
    @Override
    public String getTopic() {
        UserModel loggedInUser = this.configuration.getLoggedInUser();

        if (loggedInUser != null) {
            return String.format("user:%s", loggedInUser.getUsername());
        }

        return "user:not_logged_in";
    }

}
