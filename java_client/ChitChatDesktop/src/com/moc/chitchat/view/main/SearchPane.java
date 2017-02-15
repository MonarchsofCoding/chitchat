package com.moc.chitchat.view.main;

import com.moc.chitchat.controller.authentication.UserSearchController;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import org.tbee.javafx.scene.layout.fxml.MigPane;


/**
 *
 */
@Component
public class SearchPane implements EventHandler<ActionEvent> {

    private UserSearchController userSearchController;

    private TextField usernameField;
    private Button searchBtn;



    @Autowired
    public SearchPane(
            UserSearchController userSearchController
    ) {
        this.userSearchController = userSearchController;
    }

    public MigPane getContentPane() {
        MigPane searchPane = new MigPane();

        MigPane searchForm = new MigPane();



        this.usernameField = new TextField();
        this.usernameField.setPromptText("Find User");
        searchForm.add(this.usernameField, "span, wrap");


        this.searchBtn = new Button("Search");
        this.searchBtn.setOnAction(this);
        searchForm.add(this.searchBtn, "wrap, grow");

        searchPane.add(searchForm, "dock north");


        return searchPane;
    }

    @Override
    public void handle(ActionEvent event) {

    }
}
