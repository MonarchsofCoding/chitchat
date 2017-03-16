package com.moc.chitchat.view.main;

import com.jfoenix.controls.JFXButton;
import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.view.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;


/**
 * WestView provides the west side of the MainView.
 */
@Component
public class WestView extends BaseView implements EventHandler<ActionEvent> {

    private MigPane westPane;

    private MigPane searchPane;
    private ConversationListView conversationListView;
    private MigPane conversationListPane;

    private ToggleButton togglePaneButton;

    /**
     * WestView constructor
     * @param searchView the view for searching Users.
     * @param conversationListView the view for listing Conversations.
     */
    @Autowired
    WestView(
        SearchView searchView,
        ConversationListView conversationListView
    ) {
        this.conversationListView = conversationListView;
        searchView.setWestView(this);
        this.searchPane = searchView.getContentPane();

        this.conversationListPane = conversationListView.getContentPane();
    }

    /**
     * Returns the content pane for this view.
     * @return the content pane.
     */
    @Override
    protected MigPane getContentPane() {
        this.westPane = new MigPane();
        westPane.setLayout("fill");

        this.togglePaneButton = new ToggleButton();
        this.togglePaneButton.setOnAction(this);
        this.togglePaneButton.setMaxWidth(140);
        this.togglePaneButton.setId("west-toggle-btn");
        this.westPane.add(this.togglePaneButton, "dock north,center");
        this.showConversationListView();

        return westPane;
    }

    /**
     * Shows the search view and changes the toggle text.
     */
    private void showSearchView() {
        this.togglePaneButton.setText("Conversations");
        this.westPane.remove(this.conversationListPane);
        this.westPane.add(this.searchPane);
    }

    /**
     * Shows the conversation list view and changes the toggle text.
     */
    private void showConversationListView() {
        this.togglePaneButton.setText("Search Users");
        this.westPane.remove(this.searchPane);
        this.westPane.add(this.conversationListPane);
    }

    /**
     * Shows the conversation list view with the given conversation selected.
     * @param c the conversation to select.
     */
    void showConversationListView(Conversation c) {
        this.showConversationListView();
        this.conversationListView.setSelectedConversation(c);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        // TODO: Needs a better toggle action rather than using string comparison
        if (this.togglePaneButton.getText().equals("Search Users")) {
            this.showSearchView();
        } else if (this.togglePaneButton.getText().equals("Conversations")) {
            this.showConversationListView();
        }
    }
}
