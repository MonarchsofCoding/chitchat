package com.moc.chitchat.channel.handler;

import com.moc.chitchat.application.Configuration;
import javafx.application.Platform;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserLogoutHandler provides the handler that is called when user:* receives user:logout.
 */
@Component
public class UserLogoutHandler implements HandlerInterface {

    private Configuration configuration;

    @Autowired
    UserLogoutHandler(
        Configuration configuration
    ) {
        this.configuration = configuration;
    }

    /**
     * Returns the event this handler handles.
     * @return the event this handler handles.
     */
    @Override
    public String getEvent() {
        return "user:logout";
    }

    /**
     * Handles the payload for this event.
     * @param payload The payload attached to the event.
     */
    @Override
    public void handleMessage(JSONObject payload) {
        System.out.println("Someone else has logged in!");
        Platform.runLater(() -> {
            this.configuration.logout();
        });
    }
}
