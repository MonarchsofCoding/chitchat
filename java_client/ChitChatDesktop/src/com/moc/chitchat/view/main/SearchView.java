package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.BaseView;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

    private WestView westView;

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

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Find User");
        this.usernameField.setOnAction(this);
        MigPane searchForm = new MigPane();
        searchForm.add(this.usernameField, "span, grow");

        this.searchBtn = new Button("Search");
        this.searchBtn.setOnAction(this);
        searchForm.add(this.searchBtn, "span, grow");

        this.observableUserList = FXCollections.observableArrayList();
        this.searchList = new ListView<>(this.observableUserList);
        searchForm.add(this.searchList, "span, grow");

        this.startConversationBtn = new Button("Start Chat");
        this.startConversationBtn.setOnAction(this);
        searchForm.add(this.startConversationBtn, "span");

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
            List<UserModel> listUsers = this.userSearchController.searchUser(this.usernameField.getText());
            this.observableUserList.addAll(listUsers);

        } catch (UnirestException unirestException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(unirestException.getMessage());
            alert.show();
        } catch (ValidationException validationException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(validationException.getErrors().getFieldError("username").getDefaultMessage());
            alert.show();
        } catch (UnexpectedResponseException unexpectedResponseException) {
            unexpectedResponseException.printStackTrace();
        }
    }

    /**
     * Starts a conversation using the selected user from the user list.
     */
    private void startConversation() {
        UserModel selectedUser = this.searchList.getSelectionModel().getSelectedItem();
        // TODO: show error if no user is selected (selectedUser == null)

        System.out.println(String.format("Starting conversation with: %s", selectedUser));
        Conversation conversation = this.chitChatData.getConversation(selectedUser);
        this.westView.showConversationListView(conversation);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {
            this.searchAction();
        } else if (event.getSource() == this.startConversationBtn) {
            this.startConversation();
        }
    }
}
