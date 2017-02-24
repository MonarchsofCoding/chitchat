package com.moc.chitchat.view.main;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.controller.ChatController;
import com.moc.chitchat.controller.UserSearchController;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.AuthenticationStage;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import javax.xml.soap.Text;
import java.util.List;

/**
 * Created by spiros on 24/02/2017.
 */
public class ChatListPane extends BaseView {

    private ChatController chatController;
    private ListView<Conversation> chatuserlist;
    private ObservableList<String> names;
    private MainStage stage;


    @Autowired
    public ChatListPane(
            ChatController ChatController
    ) {
        this.chatController = chatController;
    }


    public void setStage(MainStage stage) {
        this.stage = stage;
    }



    public MigPane getContentPane() {
        MigPane chatlistPane = new MigPane();

        MigPane chatlistForm = new MigPane();






        this.chatuserlist = new ListView<Conversation>();
        chatlistForm.add(this.chatuserlist,"grow,cell 0 1");
        this.chatuserlist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("you clicked the "+chatuserlist.getSelectionModel().getSelectedItem());

            }
        });



        chatlistPane.add(chatlistForm, "dock south");

        return chatlistPane;
    }







}
