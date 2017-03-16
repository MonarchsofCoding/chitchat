package com.moc.chitchat.view.main;

import com.moc.chitchat.view.BaseView;
import com.sun.glass.ui.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.awt.*;

import static javafx.scene.input.KeyCode.M;

/**
 * MainView provides the main authenticated user view.
 */
@Component
public class MainView extends BaseView {

    private WestView westView;
    private ConversationView conversationView;

    @Autowired
    public MainView(
        WestView westView,
        ConversationView conversationView
    ) {
        this.westView = westView;
        this.conversationView = conversationView;
    }

    @Override
    protected MigPane getContentPane() {
        this.baseStage.setWindowTitle("Chit Chat");

        MigPane basePane = new MigPane();
        basePane.setId("main-view-pane");
        basePane.setLayout("fill");
        MigPane westPane = westView.getContentPane();
        basePane.add(westPane, "dock west");
        basePane.add(conversationView.getContentPane(), "grow");

        return basePane;
    }
}
