package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

    private MessageController messageController;

    private TextField newMessageField;

    public ConversationView(MessageController messageController) {
        this.messageController = messageController;
    }

    /**
     * Returns the content pane for this view.
     * It is by default empty at the start whilst no conversation is selected.
     * TODO: we could add a nice message or instruction telling the user to select a conversation first.
     *
     * @return the content pane
     */
    @Override
    protected MigPane getContentPane() {
        this.conversationPane = new MigPane();

        return this.conversationPane;
    }

    /**
     * Updates the content pane to show the messages in the given conversation.
     *
     * @param c the conversation to show.
     */
    void showConversation(Conversation c) {
        this.conversationPane.getChildren().clear(); // Clear the conversation view

        this.conversation = c;
        for (Node n : this.conversationPane.getChildren()) {
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
        try {
            Message message = this.messageController.send(
                    this.conversation.getOtherParticipant(),
                    this.newMessageField.getText());

            this.messages.add(message);
        } catch (UnirestException unirestException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(unirestException.getMessage());
            alert.show();
        } catch (ValidationException validationException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(validationException.getErrors().getFieldError("errors").getDefaultMessage());
            alert.show();
        } catch (UnexpectedResponseException unexpectedResponse) {
            unexpectedResponse.printStackTrace();
        }

        this.newMessageField.clear();
        // TODO: we will probably need to set the Messages list in Conversation to be an ObservableList.
    }
}