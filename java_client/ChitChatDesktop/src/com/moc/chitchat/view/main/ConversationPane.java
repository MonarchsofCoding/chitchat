package com.moc.chitchat.view.main;

import com.moc.chitchat.view.authentication.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

@Component
public class ConversationPane extends BaseView implements EventHandler<ActionEvent> {

    TextArea messages;

    TextField newMessageField;

    @Override
    protected MigPane getContentPane() {
        MigPane conversationPane = new MigPane();

        this.messages = new TextArea();
        conversationPane.add(this.messages, "span");

        this.newMessageField = new TextField();
        this.newMessageField.setPromptText("Enter Message: ");

        conversationPane.add(this.newMessageField, "span");

        return conversationPane;
    }

    @Override
    public void handle(ActionEvent event) {

    }
}