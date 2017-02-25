package com.moc.chitchat.view.main;


import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.view.authentication.BaseView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

@Component
public class ConversationListView extends BaseView {

    private ChitChatData chitChatData;

    private ListView<Conversation> conversationListView;

    public ConversationListView(
        ChitChatData chitChatData
    ) {
        this.chitChatData = chitChatData;
    }

    public MigPane getContentPane() {
        MigPane chatListPane = new MigPane();

        this.conversationListView = new ListView<>(this.chitChatData.getConversations());

        chatListPane.setLayout("fill");
        chatListPane.add(this.conversationListView, "span");

        return chatListPane;
    }







}
