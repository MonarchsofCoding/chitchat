package com.moc.chitchat.client;

import com.moc.chitchat.channel.ChannelInterface;
import com.moc.chitchat.channel.UserChannel;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * SocketListener provides the primary web socket listener.
 */
@Component
public class SocketListener extends WebSocketListener {

    private int ref = 0;

    private Map<String, ChannelInterface> channels;

    /**
     * SocketListener constructor.
     * @param userChannel the UserMessage channel.
     */
    @Autowired
    public SocketListener(
        UserChannel userChannel
    ) {
        this.channels = new HashMap<>();
        this.channels.put("user.channel", userChannel);
    }

    /**
     * onOpen event is fired when the web socket is connected.
     * This is used to connect to the selected channels on the socket.
     *
     * @param webSocket the web socket client.
     * @param response the response from the web socket server.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        // Join channels
        for (ChannelInterface channel: this.channels.values()) {
            JSONObject channelJoin = channel.getJoin();
            channelJoin.put("ref", this.makeRef());

            webSocket.send(channelJoin.toString());
        }
    }

    /**
     * onMessage event is fired when there is a message from the web socket.
     * The message payload is forwarded onto the relevant channel handler.
     *
     * @param webSocket the web socket client.
     * @param text The text response from the server.
     */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        System.out.println("WS sent: " + text);
        JSONObject jsonMsg = new JSONObject(text);

        String event = jsonMsg.getString("topic");

        // Send payload to relevant eventHandler
        for (ChannelInterface channel: this.channels.values()) {
            if (event.equals(channel.getTopic())) {
                try {
                    channel.handleMessage(jsonMsg.getString("event"), jsonMsg.getJSONObject("payload"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * onClosing is fired when the web socket is closed.
     *
     * @param webSocket the web socket client.
     * @param code the status code.
     * @param reason the reason the web socket was closed.
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    /**
     * onFailure is fired when there is an error on the web socket
     * @param webSocket the web socket client.
     * @param thrown the status code.
     * @param response the response from the web socket server.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable thrown, Response response) {
        thrown.printStackTrace();
    }


    /**
     * makeRef creates a new reference for use in Phoenix Channels.
     * Implementation from: https://github.com/eoinsha/JavaPhoenixChannels/blob/a854213bccb73c5a6088f2cb71cd1d752e5b4336/src/main/java/org/phoenixframework/channels/Socket.java#L350
     *
     * @return A new reference.
     */
    private String makeRef() {
        if (this.ref == Integer.MAX_VALUE) {
            this.ref = 0;
        }
        this.ref++;

        return Integer.toString(this.ref);
    }

}
