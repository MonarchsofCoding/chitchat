package com.moc.chitchat.application;

import com.moc.chitchat.model.Conversation;
import com.moc.chitchat.model.UserModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * ChitChatData provides the main application states
 */
@Component
public class ChitChatData {

    private ObservableList<Conversation> conversations;
//    private Conversation activeConversation;
    private Conversation activeConversation;

    public ChitChatData() {
        this.conversations = FXCollections.observableArrayList();
    }

    public void setActiveConversation(UserModel user) {
        Conversation c = this.findConversation(user);

        if (c == null) {
            c = new Conversation(user);
            System.out.println(String.format("Adding conversation for: %s", user));
            this.conversations.add(c);
        }

        this.activeConversation = c;
    }

    public Conversation getActiveConversation() {
        return this.activeConversation;
    }

    public ObservableList<Conversation> getConversations() {
        return this.conversations;
    }

    private Conversation findConversation(UserModel user) {
        for (Conversation c: this.conversations) {
            if (c.getOtherParticipant().equals(user)) {
                return c;
            }
        }

        return null;
    }

}
