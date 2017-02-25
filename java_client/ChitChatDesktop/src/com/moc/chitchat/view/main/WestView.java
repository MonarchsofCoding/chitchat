package com.moc.chitchat.view.main;

import com.moc.chitchat.view.authentication.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;


/**
 * WestView provides the west side of the Main application stage.
 */
@Component
public class WestView extends BaseView implements EventHandler<ActionEvent> {

    private MigPane westPane;

    private MigPane searchPane;
    private MigPane conversationListPane;

    private Button togglePaneButton;

    @Autowired
    WestView(
        SearchView searchView,
        ConversationListView conversationListView
    ) {
        searchView.setWestView(this);
        this.searchPane = searchView.getContentPane();

        this.conversationListPane = conversationListView.getContentPane();
    }

    @Override
    protected MigPane getContentPane() {
        this.westPane = new MigPane();
        westPane.setLayout("fill");

        this.togglePaneButton = new Button();
        this.togglePaneButton.setOnAction(this);
        this.westPane.add(this.togglePaneButton, "dock north");
        this.setConversationListView();

        return westPane;
    }

    private void setSearchView() {
        this.togglePaneButton.setText("Conversations");
        this.westPane.remove(this.conversationListPane);
        this.westPane.add(this.searchPane);
    }

    public void setConversationListView() {
        this.togglePaneButton.setText("Search Users");
        this.westPane.remove(this.searchPane);
        this.westPane.add(conversationListPane);
    }


    @Override
    public void handle(ActionEvent actionEvent) {
        if (this.togglePaneButton.getText().equals("Search Users")) {
            this.setSearchView();
        } else {
            this.setConversationListView();
        }
    }
}
