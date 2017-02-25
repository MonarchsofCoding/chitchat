package com.moc.chitchat.view.main;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * ConversationView provides the view for viewing messages in a conversation and sending more.
 */
@Component
public class ConversationView extends BaseView implements EventHandler<ActionEvent> {

    private MigPane conversationPane;

    private Conversation conversation;
    private ObservableList<Message> messages;

    private TextField newMessageField;

    /**
     * Returns the content pane for this view.
     * It is by default empty at the start whilst no conversation is selected.
     * TODO: we could add a nice message or instruction telling the user to select a conversation first.
     * @return the content pane
     */
    @Override
    protected MigPane getContentPane() {
        this.conversationPane = new MigPane();

        return this.conversationPane;
    }

    /**
     * Updates the content pane to show the messages in the given conversation.
     * @param c the conversation to show.
     */
    void showConversation(Conversation c) {
        this.conversation = c;
        for (Node n: this.conversationPane.getChildren()) {
            this.conversationPane.remove(n);
        }

        Label header = new Label(String.format("Chat with: %s", c.getOtherParticipant().getUsername()));
        this.conversationPane.add(header, "dock north");

        this.messages = FXCollections.observableArrayList(c.getMessages());
        ListView<Message> messages = new ListView<>(this.messages);
        this.conversationPane.add(messages, "span");

        this.newMessageField = new TextField();
        newMessageField.setPromptText("Enter Message: ");
        newMessageField.setOnAction(this);

        this.conversationPane.add(newMessageField, "span");
    }

    @Override
    public void handle(ActionEvent event) {

        // TODO: Replace with call to MessageController e.g. this.messageController.send(this.conversation.getOtherParticipant(), this.newMessageField.getText())
        // which should return the Message
        // that you can then do this.messages.add(message)
        this.messages.add(new Message(this.conversation.getOtherParticipant(), this.conversation.getOtherParticipant(), this.newMessageField.getText()));

        // TODO: we will probably need to set the Messages list in Conversation to be an ObservableList.
    }
}