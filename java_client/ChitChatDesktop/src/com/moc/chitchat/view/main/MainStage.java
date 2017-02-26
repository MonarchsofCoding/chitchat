package com.moc.chitchat.view.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * MainStage provides the main application window.
 */
@Component
public class MainStage extends Stage {

    /**
     * Constructor for the main application window.
     *
     * @param westView - west side of the Main application stage
     * @param conversationView - for viewing messages in a conversation and sending more.
     */
    @Autowired
    public MainStage(
            WestView westView,
            ConversationView conversationView
    ) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX / 2);
        int height = Math.round(resY / 2);

        this.setX((resX - width) / 2);
        this.setY((resY - height) / 2);


        this.setTitle("Welcome to Chit Chat ");

        MigPane basePane = new MigPane();
        basePane.setLayout("fill");

        MigPane westPane = westView.getContentPane();
        basePane.add(westPane, "dock west");
        basePane.add(conversationView.getContentPane(), "grow");

        this.setScene(new Scene(basePane));
    }

}
