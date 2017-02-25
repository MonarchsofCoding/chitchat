package com.moc.chitchat.view.main;


import com.moc.chitchat.application.ChitChatData;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.view.authentication.BaseView;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * ConversationListView provides the list of current active conversations.
 */
@Component
public class ConversationListView extends BaseView {

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
        MigPane chatListPane = new MigPane();

        this.conversationListView = new ListView<>(this.chitChatData.getConversations());

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
