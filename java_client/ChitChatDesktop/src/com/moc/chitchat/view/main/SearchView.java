package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.BaseView;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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
    private Label errormessage;
    private WestView westView;
    private Label errorusermessage;
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
        this.errorusermessage = new Label();
        this.errorusermessage.setTextFill(Color.RED);
        this.errorusermessage.setId("errorusermsgsearch");
        this.errorusermessage.setVisible(false);

        this.usernameField = new TextField();
        this.usernameField.setId("usernameFieldSearch");
        this.usernameField.setPromptText("Find User");
        this.usernameField.setOnAction(this);
        MigPane searchForm = new MigPane();
        searchForm.add(this.errorusermessage,"span,wrap");
        searchForm.add(this.usernameField, "span, grow");

        this.searchBtn = new Button("Search");
        this.searchBtn.setId("searchBtn");
        this.searchBtn.setOnAction(this);
        searchForm.add(this.searchBtn, "span, grow");

        this.observableUserList = FXCollections.observableArrayList();
        this.searchList = new ListView<>(this.observableUserList);
        searchForm.add(this.searchList, "span, grow");

        this.startConversationBtn = new Button("Start Chat");
        this.startConversationBtn.setId("StartChatBtn");
        this.startConversationBtn.setOnAction(this);
        searchForm.add(this.startConversationBtn, "span, wrap");

        this.errormessage = new Label();
        this.errormessage.setId("errormessageSearch");
        this.errormessage.setVisible(false);
        this.errormessage.setTextFill(Color.RED);
        searchForm.add(this.errormessage,"span");

        MigPane searchPane = new MigPane();
        searchPane.add(searchForm, "grow");

        return searchPane;
    }

    /**
     * Searches for users using the parameters set.
     */
    private void searchAction() {
        try {
            this.observableUserList.clear();
            this.errorusermessage.setVisible(false);
            List<UserModel> listUsers = this.userSearchController.searchUser(this.usernameField.getText());
            this.observableUserList.addAll(listUsers);
            if(this.observableUserList.isEmpty()){
                this.errorusermessage.setText("No User Available");
                this.errorusermessage.setVisible(true);
            }
        } catch (UnirestException unirestException) {

            this.errorusermessage.setText(unirestException.getMessage());
            this.errorusermessage.setVisible(true);
        } catch (ValidationException validationException) {
            this.errorusermessage.setText(validationException.getErrors().getFieldError("username").getDefaultMessage());
            this.errorusermessage.setVisible(true);
        } catch (UnexpectedResponseException unexpectedResponseException) {
            this.errorusermessage.setText("Error from Server");
            this.errorusermessage.setVisible(true);
            unexpectedResponseException.printStackTrace();
        }
    }

    /**
     * Starts a conversation using the selected user from the user list.
     */
    private void startConversation() {
        UserModel selectedUser = this.searchList.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {

            this.errormessage.setText("No user was selected");
            this.errormessage.setVisible(true);

        } else {
            System.out.println(String.format("Starting conversation with: %s", selectedUser));
            this.errormessage.setVisible(false);
            Conversation conversation = this.chitChatData.getConversation(selectedUser);
            this.westView.showConversationListView(conversation);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {
            this.errorusermessage.setVisible(false);
            this.errormessage.setVisible(false);
            this.searchAction();
        } else if (event.getSource() == this.startConversationBtn) {
            this.errorusermessage.setVisible(false);
            this.errormessage.setVisible(false);
            this.startConversation();
        }
    }
}
