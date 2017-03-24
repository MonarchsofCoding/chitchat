package com.moc.chitchat.view.main;

import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.MessageHelper;
import com.moc.chitchat.view.helper.UserHelper;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * ConversationListViewTest provides tests for the list of conversations.
 */
public class ConversationListViewTest extends PrimaryStageTest {

    public static final String conversationUserList = "#conversation-user-list";

    /**
     * Test to start Conversation with one user and check the fields of chat recognistion (header)
     */
    @Test
    public void test_conversation_with_user_list() throws Exception {
        UserHelper.createUser(this, "conversationList_user", "user1234");
        UserHelper.createUser(this, "conversationList_user2", "user1234");
        MessageHelper.loginUser("conversationList_user2","user1234");
        UserHelper.loginUser(this, "conversationList_user", "user1234");
        UserModel userModel1 = new UserModel("") ;

        clickOn(WestViewTest.togglePaneBtn);

        clickOn(SearchViewTest.usernameFld).write("conversationList_user2");
        clickOn(SearchViewTest.searchBtn);
        clickOn("conversationList_user2");
        clickOn(SearchViewTest.chatBtn);

        Thread.sleep(500);
        verifyThat(conversationUserList, NodeMatchers.isVisible());
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user2"));
    }

    /**
     * Test the creation of conversation with two different users and change conversations chats
     */
    @Test
    public void test_change_conversation_with_user_list() throws Exception {
        UserHelper.createUser(this, "conversationList_user3", "user1234");
        UserHelper.createUser(this, "conversationList_user4", "user1234");
        UserHelper.createUser(this, "conversationList_user5", "user1234");


        MessageHelper.loginUser("conversationList_user4","user1234");
        Thread.sleep(200);
        MessageHelper.loginUser("conversationList_user5","user1234");
        Thread.sleep(200);
        UserHelper.loginUser(this, "conversationList_user3", "user1234");
        Thread.sleep(1000);
        clickOn(WestViewTest.togglePaneBtn);

        clickOn(SearchViewTest.usernameFld).write("conversationList_user4");
        clickOn(SearchViewTest.searchBtn);
        clickOn("conversationList_user4");
        clickOn(SearchViewTest.chatBtn);
        Thread.sleep(200);
        verifyThat(conversationUserList, NodeMatchers.isVisible());
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user4"));

        clickOn(WestViewTest.togglePaneBtn);
        clickOn(SearchViewTest.usernameFld).write("conversationList_user5");
        clickOn(SearchViewTest.searchBtn);
        clickOn("conversationList_user5");
        clickOn(SearchViewTest.chatBtn);
        Thread.sleep(500);
        verifyThat(conversationUserList, NodeMatchers.isVisible());
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user5"));
        Thread.sleep(500);

        clickOn("conversationList_user4");
        Thread.sleep(500);
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user4"));

        clickOn("conversationList_user5");
        Thread.sleep(500);
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user5"));

        clickOn("conversationList_user4");
        Thread.sleep(500);
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user4"));

        clickOn("conversationList_user5");
        Thread.sleep(500);
        verifyThat(ConversationViewTest.chatHeaderLbl, NodeMatchers.hasText("Chat with: conversationList_user5"));
    }

}
