package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.tbee.javafx.scene.layout.fxml.MigPane;
import com.moc.chitchat.exception.ValidationException;

import java.util.List;


/**
 *
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

    @Autowired
    public SearchView(
        UserSearchController userSearchController,
        ChitChatData chitChatData
    ) {
        this.userSearchController = userSearchController;
        this.chitChatData = chitChatData;
    }

    public void setWestView(WestView westView) {
        this.westView = westView;
    }

    public MigPane getContentPane() {
        MigPane searchPane = new MigPane();

        MigPane searchForm = new MigPane();

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Find User");
        this.usernameField.setOnAction(this);
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

        searchPane.add(searchForm, "grow");

        return searchPane;
    }

    private void searchAction() {
        try {
            this.observableUserList.clear();
            List<UserModel> listUsers = this.userSearchController.searchUser(this.usernameField.getText());
            this.observableUserList.addAll(listUsers);

        } catch (UnirestException unirestException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(unirestException.getMessage());
            alert.show();
        } catch (UnexpectedResponseException unexpectedResponseException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unauthorized");
            alert.show();
        } catch (ValidationException validationException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(validationException.getErrors().getFieldError("username").getDefaultMessage());
            alert.show();
        }
    }

    private void startConversation() {
        UserModel selectedUser = this.searchList.getSelectionModel().getSelectedItem();
        System.out.println(String.format("Starting conversation with: %s", selectedUser));

        this.chitChatData.setActiveConversation(selectedUser);
        this.westView.setConversationListView();
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
