package com.moc.chitchat.view.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.view.BaseView;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ChitChatData chitChatData;

    private TextField newMessageField;
    private Label errormessage;
    private Button sendbtn;

    @Autowired
    public ConversationView(MessageController messageController,
                            ChitChatData chitChatData) {
        this.messageController = messageController;
        this.chitChatData = chitChatData;
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
        this.conversationPane.setLayout("fill");

        return this.conversationPane;
    }

    /**
     * Updates the content pane to show the messages in the given conversation.
     *
     * @param c the conversation to show.
     */
    void showConversation(Conversation c) {
        this.conversationPane.getChildren().clear(); // Clear the conversation view
        this.conversationPane.setId("conversation-pane");

        if (c == null) {
            return;
        }

        this.conversation = c;

        for (Node n : this.conversationPane.getChildren()) {
            this.conversationPane.remove(n);
        }

        Label header = new Label(String.format("Chat with: %s", c.getOtherParticipant().getUsername()));
        header.setId("conversation-chatHeader-lbl");
        this.conversationPane.add(header, "dock north");
        this.messages = FXCollections.observableArrayList(c.getMessages());

        JFXListView<Message> viewMessages = new JFXListView<>();
        viewMessages.setItems(c.getMessages());
        c.getMessages().addListener((ListChangeListener<Message>) change -> {
            viewMessages.scrollTo(c.getMessages().size() - 1);
        });
        viewMessages.setId("conversation-messages-list");
        this.conversationPane.add(viewMessages, "span, growx");

        this.newMessageField = new JFXTextField();
        this.newMessageField.setPromptText("Enter Message: ");
        this.newMessageField.setId("conversation-message-fld");
        this.newMessageField.setOnAction(this);
        this.conversationPane.add(newMessageField, "span, growx");

        this.errormessage = new Label();
        this.errormessage.setId("conversation-error-lbl");
        this.errormessage.setVisible(false);
        this.conversationPane.add(errormessage, "span, growx");

        this.sendbtn = new JFXButton("Send");
        this.sendbtn.setId("conversation-send-btn");

        this.sendbtn.setOnAction(this);
        this.conversationPane.add(sendbtn, "span, align right");
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.sendbtn || event.getSource() == this.newMessageField) {
            try {
                Message message = this.messageController.send(
                        this.conversation.getOtherParticipant(),
                        this.newMessageField.getText());

                this.errormessage.setVisible(false);

                this.messages.add(message);
            } catch (ValidationException validationException) {
                String mesg = validationException.getErrors().getFieldError("message").getDefaultMessage();
                errormessage.setText(mesg);
                errormessage.setVisible(true);

            } catch (UnexpectedResponseException unexpectedResponse) {
                errormessage.setText("Unexpected error");
                errormessage.setVisible(true);
                unexpectedResponse.printStackTrace();
            } catch (IOException ioException) {
                // I think this has to be changed
                errormessage.setText("Incorrect input");
                errormessage.setVisible(true);
                ioException.printStackTrace();
            } catch (Exception expt) {
                expt.printStackTrace();
            }

            this.newMessageField.clear();
            // TODO: we will probably need to set the Messages list in Conversation to be an ObservableList.
        }
    }
}