package com.moc.chitchat.view.main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.awt.*;

/**
 * MainStage provides the main application window.
 */
@Component
public class MainStage extends Stage {

    /**
     * Constructor for the main application window.
     * @param westView
     * @param conversationView
     */
    @Autowired
    public MainStage(
        WestView westView,
        ConversationView conversationView
    ) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX/2);
        int height = Math.round(resY/2);

        this.setX((resX - width)/2);
        this.setY((resY - height)/2);


        this.setTitle("Welcome to Chit Chat ");

        MigPane basePane = new MigPane();
        basePane.setLayout("fill");

        MigPane westPane = westView.getContentPane();
        basePane.add(westPane, "dock west");
        basePane.add(conversationView.getContentPane(), "grow");

        this.setScene(new Scene(basePane));
    }

}
