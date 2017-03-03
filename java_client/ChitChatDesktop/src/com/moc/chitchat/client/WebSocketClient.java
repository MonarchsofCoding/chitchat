package com.moc.chitchat.client;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.controller.MessageController;
import org.phoenixframework.channels.*;
import org.springframework.stereotype.Component;

/**
 * WebSocketClient provides the full duplex communications, e.g. we are using this for receiving messages.
 */
@Component
public class WebSocketClient {

    private Configuration configuration;
    private Socket socket;
    private MessageController messageController;

    /**
     * WebSocketClient constructor.
     * @param configuration - passing the configuration so we know the current user
     */
    public WebSocketClient(Configuration configuration,
                           MessageController messageController) {
        this.configuration = configuration;
        this.messageController = messageController;
    }

    /**
     * startConnection.
     */
    public void startConnection() {

        String accessToken = this.configuration.getLoggedInUser().getAuthToken();

        try {
            socket = new Socket(String.format("ws://localhost:4000/api/v1/messages/websocket?guardian_token=%s", accessToken));
            socket.connect();
            System.out.println("connected");
            ObjectNode auth = JsonNodeFactory.instance.objectNode();
            auth.put("guardian_token", accessToken);
            Channel channel = socket.chan("user:"+this.configuration.getLoggedInUser().getUsername(), auth);
            channel.join()
                    .receive("ok", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    System.out.println("JOINED with " + envelope.toString());
                }
            });
            channel.on("new:message", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    System.out.println("NEW MESSAGE: " + envelope.toString());
                    // Calls MessageController
                    messageController.receive(envelope.getPayload().get("body").asText(),
                            envelope.getPayload().get("from").asText());
                }
            });
            System.out.println("channel is on");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean stopConnection() {
        if (socket.isConnected()) {
            System.out.println("Already disconnected");
            return true;
        } else {
            try {
                System.out.println("Attempt to disconnect");
                socket.disconnect();
                System.out.println("Success");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }



}
