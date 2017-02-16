package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.controller.authentication.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.AuthenticationStage;
import com.moc.chitchat.view.authentication.BaseView;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.util.List;


/**
 *
 */
@Component
public class SearchPane extends BaseView implements EventHandler<ActionEvent> {

    private UserSearchController userSearchController;

    private TextField usernameField,messagesend;
    private Button searchBtn,sendBtn, logoutBtn;
    private ListView<String> searchlist;
    private ObservableList<String> names;
    private TextArea DisplayMessages;
    private AuthenticationStage stage;

    @Autowired
    public SearchPane(
            UserSearchController userSearchController
    ) {
        this.userSearchController = userSearchController;
    }


    public void setStage(AuthenticationStage stage) {
               this.stage = stage;
    }



    public MigPane getContentPane() {
        MigPane searchPane = new MigPane();

        MigPane searchForm = new MigPane();


        ///Search AREA @@@@@@@@@@@???????????????????/////
        this.usernameField = new TextField();
        this.usernameField.setPromptText("Find User");
        searchForm.add(this.usernameField, "span, cell 0 2 , align left");

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

        //Chat- AREA""""""""

        this.DisplayMessages = new TextArea();
        this.DisplayMessages.setPromptText("Empty Text Area");
        searchForm.add(this.DisplayMessages, "cell 1 1 ,grow");

        this.messagesend = new TextField();
        this.messagesend.setPromptText("Type your message");
        searchForm.add(this.messagesend,"cell 1 2 ,grow");

        this.sendBtn = new Button("Send");
        this.sendBtn.setOnAction(this);
        searchForm.add(this.sendBtn,"cell 1 3 , align right ");

        //****Menu Bar****//
        this.logoutBtn = new Button("logout");
        this.logoutBtn.setOnAction(this);
        searchForm.add(this.logoutBtn,"cell 0 0 , align left");




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
            alert.setHeaderText("Server error connection");
            alert.show();
            e.printStackTrace();
        } catch (UnexpectedResponseException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setHeaderText("Username must contain at least 3 characters");
               alert.show();

        }

    }



    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.searchBtn) {
           searchlist.getItems().clear();
           this.searchAction();
        }
        if (event.getSource()== this.sendBtn){
            System.out.println("you clicked send");
        }
        if(event.getSource()==this.logoutBtn){

           //we have to connect the logout with login screen
        }
    }
}
