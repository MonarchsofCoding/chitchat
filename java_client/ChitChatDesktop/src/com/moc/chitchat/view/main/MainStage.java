package com.moc.chitchat.view.main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * MainStage provides the main application window.
 */
@Component
public class MainStage {

    private Stage primaryStage;

    private WestView westView;
    private ConversationView conversationView;

    /**
     * Constructor for the main application window.
     *
     * @param westView         - west side of the Main application stage
     * @param conversationView - for viewing messages in a conversation and sending more.
     */
    @Autowired
    public MainStage(
            WestView westView,
            ConversationView conversationView
    ) {
        this.westView = westView;
        this.conversationView = conversationView;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * shows the main scene.
     */
    public void show() {
        this.primaryStage.setTitle("Chit Chat");

        MigPane basePane = new MigPane();
        basePane.setLayout("fill");

        MigPane westPane = westView.getContentPane();
        basePane.add(westPane, "dock west");
        basePane.add(conversationView.getContentPane(), "grow");

        this.primaryStage.setScene(new Scene(basePane));
    }

}
