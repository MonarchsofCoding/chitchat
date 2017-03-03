package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.view.BaseView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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
    private ListView<Message> updateMessages;

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
        header.setId("headerChat");
        this.conversationPane.add(header, "dock north");

        this.messages = FXCollections.observableArrayList(c.getMessages());
        ListView<Message> messages = new ListView<>(this.chitChatData
                .getConversation(this.conversation.getOtherParticipant()).getMessages());
        this.conversationPane.add(messages, "span");
        this.newMessageField = new TextField();
        this.newMessageField.setPromptText("Enter Message: ");
        this.newMessageField.setId("newmessageField");
        this.newMessageField.setOnAction(this);
        this.conversationPane.add(newMessageField, "span,grow");

        this.errormessage = new Label();
        this.errormessage.setTextFill(Color.RED);
        this.errormessage.setId("errormessage");
        this.errormessage.setVisible(false);
        this.conversationPane.add(errormessage, "span,grow,wrap");

        this.sendbtn = new Button("Send");
        this.sendbtn.setId("sendBtnmsg");
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
            } catch (UnirestException unirestException) {

                errormessage.setText(unirestException.getMessage());
                errormessage.setVisible(true);
            } catch (ValidationException validationException) {
                String mesg = validationException.getErrors().getFieldError("message").getDefaultMessage().toString();
                errormessage.setText(mesg);
                errormessage.setVisible(true);

            } catch (UnexpectedResponseException unexpectedResponse) {
                errormessage.setText("Unexpected error");
                errormessage.setVisible(true);
                unexpectedResponse.printStackTrace();
            }

            this.newMessageField.clear();
            // TODO: we will probably need to set the Messages list in Conversation to be an ObservableList.
        }
    }
}