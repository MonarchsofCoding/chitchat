package com.moc.chitchat.client;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.channel.UserMessageChannel;
import com.moc.chitchat.model.UserModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.Socket;
import org.springframework.stereotype.Component;

/**
 * WebSocketClient provides the full duplex communications, e.g. we are using this for receiving messages.
 */
@Component
public class WebSocketClient {

    private Configuration configuration;
    private Map<String, Channel> channels;
    private UserMessageChannel userMessageChannel;

    /**
     * WebSocketClient constructor.
     *
     * @param configuration - passing the configuration so we know the current user
     */
    public WebSocketClient(
        Configuration configuration,
        UserMessageChannel userMessageChannel
    ) {
        this.configuration = configuration;
        this.channels = new HashMap<>();
        this.userMessageChannel = userMessageChannel;
    }

    /**
     *Connect to the user message.
     * @param userModel it is the parameter to understand.
     * @throws IOException the exception that throws.
     */
    public void connectToUserMessage(UserModel userModel) throws IOException {
        String url = String.format("%s/api/v1/messages/websocket?authToken=%s",
            this.configuration.getBackendAddress().replace("http", "ws"),
            userModel.getAuthToken()
        );

        Socket socket = new Socket(url);
        socket.connect();

        Channel channel = this.userMessageChannel.join(socket, userModel);
        this.channels.put(String.format("chitchat.%s", userModel.getUsername()), channel);
    }

    /**
     * This function stops all if something goes wrong.
     * @throws IOException the exception in a problem situation.
     */
    public void stopAll() throws IOException {
        for(Channel c:this.channels.values()) {
            c.off("aa");
            c.leave();
            c.getSocket().disconnect();
        }
    }

}
