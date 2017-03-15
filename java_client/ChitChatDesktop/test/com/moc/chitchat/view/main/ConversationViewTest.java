package com.moc.chitchat.view.main;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.MessageHelper;
import com.moc.chitchat.view.helper.UserHelper;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * ConversationViewTest provides tests for conversation views,
 */
public class ConversationViewTest extends PrimaryStageTest {

    public final static String chatHeaderLbl = "#conversation-chatHeader-lbl";
    public final static String messagesList = "#conversation-messages-list";
    public final static String newMessageFld = "#conversation-message-fld";
    public final static String errorMessageLbl = "#conversation-error-lbl";
    public final static String sendBtn = "#conversation-send-btn";

    /**
     * Verify the existence of the conversation fields
     */
    @Test
    public void test_conversation_fields_exist() {
        UserHelper.createUser(this, "conversationView_user1", "user1234");
        UserHelper.createUser(this, "conversationView_user2", "user1234");
        UserHelper.loginUser(this, "conversationView_user1", "user1234");

        clickOn(WestViewTest.togglePaneBtn);
        clickOn(SearchViewTest.usernameFld).write("conversationView_user2");
        clickOn(SearchViewTest.searchBtn);
        clickOn("conversationView_user2");
        clickOn(SearchViewTest.chatBtn);
        verifyThat(chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationView_user2"));

        verifyThat(newMessageFld, NodeMatchers.isVisible());
        verifyThat(errorMessageLbl, NodeMatchers.isInvisible());
        verifyThat(sendBtn, NodeMatchers.isVisible());
        verifyThat(sendBtn, NodeMatchers.hasText("Send"));
    }

    /**
     * Tests sending a message updates the message list view.
     * @throws InterruptedException
     */
    @Test
    public void test_sending_message() throws InterruptedException {
        UserHelper.createUser(this, "conversationView_user3", "user1234");
        UserHelper.createUser(this, "conversationView_user4", "user1234");
        UserHelper.loginUser(this, "conversationView_user3", "user1234");

        clickOn(WestViewTest.togglePaneBtn);
        clickOn(SearchViewTest.usernameFld).write("conversationView_user4");
        clickOn(SearchViewTest.searchBtn);
        Thread.sleep(500);
        clickOn("conversationView_user4");
        clickOn(SearchViewTest.chatBtn);
        Thread.sleep(500);
        verifyThat(chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationView_user4"));

        clickOn(newMessageFld).write("Hello!");
        clickOn(sendBtn);
        Thread.sleep(500);
        verifyThat(messagesList, NodeMatchers.isVisible());
        verifyThat("conversationView_user3: Hello!", NodeMatchers.isVisible());
    }

    /**
     * Tests that receiving a message updates the message list view.
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void test_receiving_message() throws InterruptedException, IOException {
        UserHelper.createUser(this, "conversationView_user5", "user1234");
        UserHelper.createUser(this, "conversationView_user6", "user1234");
        UserHelper.loginUser(this, "conversationView_user5", "user1234");

        clickOn(WestViewTest.togglePaneBtn);
        clickOn(SearchViewTest.usernameFld).write("conversationView_user6");
        clickOn(SearchViewTest.searchBtn);
        Thread.sleep(500);
        clickOn("conversationView_user6");
        clickOn(SearchViewTest.chatBtn);
        Thread.sleep(500);
        verifyThat(chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationView_user6"));

        // Send message as conversationView_user6
        MessageHelper.sendMessage(
            "conversationView_user6",
            "user1234",
            "conversationView_user5",
            "Hello!"
        );

        Thread.sleep(3000);

        verifyThat("conversationView_user6: Hello!", NodeMatchers.isVisible());
    }
}
