package com.moc.chitchat.view.main;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.UserHelper;
import javafx.scene.Node;
import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * ConversationListViewTest provides tests for the list of conversations.
 */
public class ConversationListViewTest extends PrimaryStageTest {


    final static String usernameFld = "#search-username-fld";
    final static String searchBtn = "#search-Btn";
    final static String ChatBtn = "#search-chat-Btn";
    final static String togglebutton ="#ToggleBtn";
    final static String headerChat = "#headerChat";



    @Before
    public void enterRegistrationView() {
        System.out.println("Entering ConversationListView");

    }

    /**
     * Test to start Conversation with one user and check the fields of chat recognistion (header)
     *
     */
   @Test
    public void addConversationWithOneUser(){
       UserHelper.createUser(this,"login_validUser","validPassword");
       UserHelper.createUser(this,"login_validUser_two","validPassword_two");
       UserHelper.loginUser(this,"login_validUser","validPassword");
       clickOn(togglebutton);
       verifyThat(togglebutton,NodeMatchers.hasText("Conversations"));
       clickOn(usernameFld).write("login_validUser");
       clickOn(searchBtn);
       clickOn("login_validUser_two");
       clickOn(ChatBtn);
       verifyThat(togglebutton,NodeMatchers.hasText("Search Users"));
       verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_two"));

    }

    /**
     * Test the creation of conversation with two different users and change conversations chats
     */
    @Test
    public void addConversationWithTwoUsers(){
        UserHelper.createUser(this,"login_validUser_three","validPassword_three");
        UserHelper.loginUser(this,"login_validUser","validPassword");
        clickOn(togglebutton);
        clickOn(usernameFld).write("login_validUser");
        clickOn(searchBtn);
        clickOn("login_validUser_two");
        clickOn(ChatBtn);
        clickOn(togglebutton);
        verifyThat(togglebutton,NodeMatchers.hasText("Conversations"));
        clickOn(usernameFld).write("login_validUser");
        clickOn(searchBtn);
        clickOn("login_validUser_three");
        clickOn(ChatBtn);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_three"));
        clickOn("login_validUser_two");
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_two"));

    }


}
