package com.moc.chitchat.view.main;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.UserHelper;
import javafx.scene.input.KeyCode;
import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import javax.swing.*;

import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_ENTER;
import static javafx.scene.input.KeyCode.ENTER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * ConversationViewTest provides tests for conversation views,
 */
public class ConversationViewTest extends PrimaryStageTest {

    final static String usernameFld = "#search-username-fld";
    final static String searchBtn = "#search-Btn";
    final static String ChatBtn = "#search-chat-Btn";
    final static String togglebutton ="#ToggleBtn";
    final static String headerChat = "#headerChat";
    final static String newmessage = "#conversation-message-fld";
    final static String errormessage = "#conversation-error-message";
    final static String sendbtn = "#conversation-send-Btn";

    @Before
    public void enterRegistrationView() {
        System.out.println("Entering ConversationView");

    }
    /**
     * Verify the existance of the conversation fields
     */
    @Test
    public void testFields(){
        UserHelper.createUser(this,"login_validUser","validPassword");
        UserHelper.createUser(this,"login_validUser_two","validPassword_two");
        UserHelper.loginUser(this,"login_validUser","validPassword");
        clickOn(togglebutton);
        clickOn(usernameFld).write("login_validUser");
        clickOn(searchBtn);
        clickOn("login_validUser_two");
        clickOn(ChatBtn);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_two"));
        verifyThat(newmessage,NodeMatchers.isVisible());
        verifyThat(errormessage,NodeMatchers.isInvisible());
        verifyThat(sendbtn,NodeMatchers.isVisible());
        verifyThat(sendbtn,NodeMatchers.hasText("Send"));
        clickOn(sendbtn);
        verifyThat(errormessage,NodeMatchers.isVisible());
        verifyThat(errormessage,NodeMatchers.hasText("can't be blank"));
    }
    /**
     * Test conversation different texts with two different users
     *
     */
    @Test
    public void testDifferentConversationViewsWithTwoUsers(){
        String message_to_user_2 = "Hello User 2";
        String message_to_user_3 = "GoodMorning User 3";

        UserHelper.createUser(this,"login_validUser_three","validPassword_three");
        UserHelper.loginUser(this,"login_validUser","validPassword");
        clickOn(togglebutton);
        clickOn(usernameFld).write("login_validUser");
        clickOn(searchBtn);
        clickOn("login_validUser_two");
        clickOn(ChatBtn);
        clickOn(newmessage).write(message_to_user_2);
        clickOn(sendbtn);
        clickOn("login_validUser: "+message_to_user_2);
        //start second conversation
        verifyThat(togglebutton,NodeMatchers.hasText("Search Users"));
        clickOn(togglebutton);
        clickOn(usernameFld).write("login_valid");
        clickOn(searchBtn);
        clickOn("login_validUser_three");
        clickOn(ChatBtn);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_three"));
        clickOn(newmessage).write(message_to_user_3);
        clickOn(sendbtn);
        clickOn("login_validUser: "+message_to_user_3);
        //verify the saved messages for user 2
        clickOn("login_validUser_two");
        clickOn("login_validUser: "+message_to_user_2);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_two"));
        //verify the saved messages for user 3
        clickOn("login_validUser_three");
        clickOn("login_validUser: "+message_to_user_3);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser_three"));
    }
}
