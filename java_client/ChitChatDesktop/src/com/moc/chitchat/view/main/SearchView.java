package com.moc.chitchat.view.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.BaseView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;


/**
 * SearchView provides the view for searching Users.
 */
@Component
public class SearchView extends BaseView implements EventHandler<ActionEvent> {

    private UserSearchController userSearchController;
    private ChitChatData chitChatData;

    private TextField usernameField;
    private ListView<UserModel> searchList;
    private ObservableList<UserModel> observableUserList;

    private Button searchBtn;
    private Button startConversationBtn;
    private Label errorMessage;
    private WestView westView;
    private Label errorUserMessage;

    /**
     * SearchView constructor.
     *
     * @param userSearchController the controller holding functions for searching Users.
     * @param chitChatData         the application data state.
     */

    @Autowired
    public SearchView(
            UserSearchController userSearchController,
            ChitChatData chitChatData
    ) {
        this.userSearchController = userSearchController;
        this.chitChatData = chitChatData;
    }

    /**
     * sets the WestView.
     * Used automatically switch from the SearchView to the ConversationListView.
     *
     * @param westView the west view.
     */
    void setWestView(WestView westView) {
        this.westView = westView;
    }

    /**
     * Returns the content pane for this view.
     *
     * @return the content pane.
     */
    public MigPane getContentPane() {
        this.errorUserMessage = new Label();
        this.errorUserMessage.setId("search-error-users-msg");
        this.errorUserMessage.setVisible(false);

        this.usernameField = new JFXTextField();
        this.usernameField.setId("search-username-fld");
        this.usernameField.setPromptText("Find User");
        this.usernameField.setOnAction(this);
        MigPane searchForm = new MigPane();
        searchForm.add(this.errorUserMessage, "span");
        searchForm.add(this.usernameField, "wrap");

        this.searchBtn = new JFXButton("Search");
        this.searchBtn.setId("search-btn");
        this.searchBtn.setOnAction(this);
        searchForm.add(this.searchBtn, "wrap");

        this.observableUserList = FXCollections.observableArrayList();
        this.searchList = new ListView<>();
        this.searchList.setItems(this.observableUserList);
        this.searchList.setPrefSize(140,300);
        this.searchList.setId("search-user-list");
        searchForm.add(this.searchList, " wrap, grow");

        this.startConversationBtn = new JFXButton("Start Chat");
        this.startConversationBtn.setId("search-chat-btn");
        this.startConversationBtn.setOnAction(this);
        searchForm.add(this.startConversationBtn, "span");

        this.errorMessage = new Label();
        this.errorMessage.setId("search-error-messages");
        this.errorMessage.setVisible(false);
        searchForm.add(this.errorMessage, "span");
        MigPane searchPane = new MigPane();
        searchPane.add(searchForm, "wrap");

        return searchPane;
    }

    /**
     * Clear all the errormessages and userfields.
     */
    public void clearfields() {
        this.errorUserMessage.setVisible(false);
        this.errorMessage.setVisible(false);
        this.usernameField.clear();
        this.searchList.getItems().clear();
    }

    /**
     * Searches for users using the parameters set.
     */
    private void searchAction() throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            this.observableUserList.clear();
            this.errorUserMessage.setVisible(false);
            List<UserModel> listUsers = this.userSearchController.searchUser(this.usernameField.getText());
            this.observableUserList.addAll(listUsers);
            if (this.observableUserList.isEmpty()) {
                this.errorUserMessage.setText("No User Available");
                this.errorUserMessage.setVisible(true);
            } else {
                this.searchList.requestFocus();
                this.usernameField.clear();
            }
        } catch (ValidationException validationException) {
            String errorMsg = validationException.getErrors()
                    .getFieldError("username").getDefaultMessage();
            this.errorUserMessage.setText(errorMsg);
            this.errorUserMessage.setVisible(true);
        } catch (UnexpectedResponseException unexpectedResponseException) {
            this.errorUserMessage.setText("Error from Server");
            this.errorUserMessage.setVisible(true);
            unexpectedResponseException.printStackTrace();
        } catch (IOException ioException) {
            // I think this has to be changed
            this.errorUserMessage.setText("Incorrect input");
            this.errorUserMessage.setVisible(true);
        }
    }

    /**
     * Starts a conversation using the selected user from the user list.
     */
    private void startConversation() {
        UserModel selectedUser = this.searchList.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {

            this.errorMessage.setText("No user was selected");
            this.errorMessage.setVisible(true);

        } else {
            System.out.println(String.format("Starting conversation with: %s", selectedUser));
            this.errorMessage.setVisible(false);
            Conversation conversation = this.chitChatData.getConversation(selectedUser);
            this.westView.showConversationListView(conversation);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {

            this.errorUserMessage.setVisible(false);
            this.errorMessage.setVisible(false);
            try {
                this.searchAction();
            } catch (InvalidKeySpecException | NoSuchAlgorithmException expt) {
                expt.printStackTrace();
            }
        } else if (event.getSource() == this.startConversationBtn) {
            this.errorUserMessage.setVisible(false);
            this.errorMessage.setVisible(false);
            this.startConversation();
            this.usernameField.clear();
            this.searchList.getItems().clear();
        }
    }
}
