package com.moc.chitchat.application;

import com.moc.chitchat.client.WebSocketClient;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ApplicationCloser provides the handler that is called when the application is closed.
 */
@Component
public class ApplicationCloser implements EventHandler<WindowEvent> {

    private WebSocketClient webSocketClient;

    @Autowired
    public ApplicationCloser(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public void handle(WindowEvent windowEvent) {
        Platform.exit();
        try {
            this.webSocketClient.stopAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
