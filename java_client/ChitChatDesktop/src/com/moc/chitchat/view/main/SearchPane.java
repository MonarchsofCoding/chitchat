package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.controller.authentication.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.util.List;


/**
 *
 */
@Component
public class SearchPane implements EventHandler<ActionEvent> {

    private UserSearchController userSearchController;

    private TextField usernameField;
    private Button searchBtn;
    private ListView<String> searchlist;
    private ObservableList<String> names;

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


        this.searchlist = new ListView<String>();
        searchForm.add(this.searchlist,"grow");
        this.searchlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("you clicked the "+searchlist.getSelectionModel().getSelectedItem());

            }
        });
        return searchPane;
    }

    private void searchAction(){
        try {
           List<UserModel> listusers = this.userSearchController.searchUser(this.usernameField.getText());
            names  = FXCollections.observableArrayList();
            for(int i=0;i<listusers.size(); i++) {
                names.add(listusers.get(i).getUsername());
            }




           searchlist.setItems(names);
           System.out.println(listusers.size());
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch (UnexpectedResponseException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {
           this.searchAction();
        }
    }
}
