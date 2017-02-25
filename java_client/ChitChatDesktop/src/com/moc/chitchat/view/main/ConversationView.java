package com.moc.chitchat.view.main;

import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

@Component
public class ConversationView extends BaseView implements EventHandler<ActionEvent> {

    private ChitChatData chitChatData;

    ListView<Message> messages;
    TextField newMessageField;

    public ConversationView(ChitChatData chitChatData) {
        this.chitChatData = chitChatData;
    }

    @Override
    protected MigPane getContentPane() {
        MigPane conversationPane = new MigPane();

        this.messages = new ListView<>();
        conversationPane.add(this.messages, "span");

        this.newMessageField = new TextField();
        this.newMessageField.setPromptText("Enter Message: ");
        this.newMessageField.setOnAction(this);

        conversationPane.add(this.newMessageField, "span");

        return conversationPane;
    }

    @Override
    public void handle(ActionEvent event) {
    }
}