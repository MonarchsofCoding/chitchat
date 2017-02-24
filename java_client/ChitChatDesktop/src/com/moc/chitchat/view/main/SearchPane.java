package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
public class SearchPane extends BaseView implements EventHandler<ActionEvent> {

    private UserSearchController userSearchController;

    private TextField usernameField;
    private Button searchBtn;
    private ListView<String> searchlist;
    private ObservableList<String> names;
    private MainStage stage;


    @Autowired
    public SearchPane(
            UserSearchController userSearchController
    ) {
        this.userSearchController = userSearchController;
    }


    public void setStage(MainStage stage) {
               this.stage = stage;
    }



    public MigPane getContentPane() {
        MigPane searchPane = new MigPane();

        MigPane searchForm = new MigPane();


        ///Search AREA @@@@@@@@@@@???????????????????/////
        this.usernameField = new TextField();
        this.usernameField.setPromptText("Find User");
        searchForm.add(this.usernameField, "span, cell 0 2 , align left, grow");

        this.searchBtn = new Button("Search");
        this.searchBtn.setOnAction(this);
        searchForm.add(this.searchBtn, "cell 0 3, grow");



        this.searchlist = new ListView<String>();
        searchForm.add(this.searchlist,"grow,cell 0 1");
        this.searchlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("you clicked the "+searchlist.getSelectionModel().getSelectedItem());

            }
        });

        searchPane.add(searchForm, "dock north");

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
          
        } catch (UnirestException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.show();
            e.printStackTrace();
        } catch (UnexpectedResponseException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setContentText("Username must contain at least 3 characters");
               alert.show();

        } catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unauthorized");
            alert.show();
            e.printStackTrace();
        }

    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {
           searchlist.getItems().clear();
           this.searchAction();
        }
    }
}
