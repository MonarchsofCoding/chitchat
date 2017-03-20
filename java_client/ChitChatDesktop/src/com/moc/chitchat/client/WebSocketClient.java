package com.moc.chitchat.client;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.model.UserModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.stereotype.Component;

/**
 * WebSocketClient provides the full duplex communications, e.g. we are using this for receiving messages.
 */
@Component
public class WebSocketClient implements Observer {

    private Configuration configuration;
    private Map<String, WebSocket> sockets;
    private SocketListener socketListener;

    /**
     * WebSocketClient constructor.
     *
     * @param configuration - passing the configuration so we know the current user
     */
    public WebSocketClient(
        Configuration configuration,
        SocketListener socketListener
    ) {
        this.configuration = configuration;
        this.sockets = new HashMap<>();
        this.socketListener = socketListener;
    }

    /**
     * Connect to the user message.
     * @param userModel it is the parameter to understand.
     * @throws IOException the exception that throws.
     */
    public void connectToUserMessage(UserModel userModel) throws IOException {
        String url = String.format("%s/api/v1/messages/websocket?authToken=%s",
            this.configuration.getBackendAddress().replace("http", "ws"),
            userModel.getAuthToken()
        );

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .pingInterval(500, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        WebSocket webSocket = client.newWebSocket(request, this.socketListener);
        this.sockets.put("chitchat.socket", webSocket);
        this.configuration.addObserver(this);
        client.dispatcher().executorService().shutdown();
    }

    /**
     * This function stops all if something goes wrong.
     * @throws IOException the exception in a problem situation.
     */
    public void stopAll() throws IOException {
        for(WebSocket sock: this.sockets.values()) {
            sock.close(1000, "Bye!");
        }
    }

    @Override
    public void update(Observable observable, Object obj) {
        try {
            this.stopAll();
        } catch (IOException expt) {
            expt.printStackTrace();
        }
    }
}
