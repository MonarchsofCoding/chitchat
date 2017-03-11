package com.moc.chitchat.view.main;

import com.jfoenix.controls.JFXListView;
import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.model.Conversation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * ConversationListView provides the list of current active conversations.
 */
@Component
public class ConversationListView {

    private ChitChatData chitChatData;
    private ConversationView conversationView;

    private ListView<Conversation> conversationListView;

    /**
     * Constructor for ConversationListView
     * @param chitChatData the application data state.
     * @param conversationView the view that shows conversations.
     */
    @Autowired
    public ConversationListView(
        ChitChatData chitChatData,
        ConversationView conversationView
    ) {
        this.chitChatData = chitChatData;
        this.conversationView = conversationView;
    }

    /**
     * Returns content pane for this view.
     * @return the content pane
     */

    public MigPane getContentPane() {

        this.conversationListView = new JFXListView<>();
        this.conversationListView.getItems().addAll(this.chitChatData.getConversations());

        this.conversationListView.setId("conversation-user-list");
        this.conversationListView.setPlaceholder(new Label("Add User for Conversation"));

        MultipleSelectionModel<Conversation> lvSelModel = this.conversationListView.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener((changed, oldConvo, newConvo) -> setSelectedConversation(newConvo));

        MigPane chatListPane = new MigPane();
        chatListPane.setLayout("fill");
        chatListPane.add(this.conversationListView, "span");

        return chatListPane;
    }

    /**
     * Sets the conversation that is currently selected.
     * Used by the SearchView when a new conversation is started.
     * @param c the conversation to select and show.
     */
    void setSelectedConversation(Conversation c) {
        this.conversationListView.getSelectionModel().select(c);
        this.conversationView.showConversation(c);
    }

}
