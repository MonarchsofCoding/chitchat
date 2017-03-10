package com.moc.chitchat.view.main;

import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.authentication.LoginView;
import com.moc.chitchat.view.authentication.LoginViewTest;
import com.moc.chitchat.view.helper.UserHelper;
import com.moc.chitchat.view.main.WestView;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * SearchViewTest provides tests for the search view functionality.
 */
public class SearchViewTest extends PrimaryStageTest {

    final static String usernameFld = "#search-username-fld";
    final static String searchBtn = "#search-Btn";
    final static String ChatBtn = "#search-chat-Btn";
    final static String errormessage = "#search-error-messages";
    final static String errorusermessage ="#search-error-users-msg";
    final static String togglebutton ="#ToggleBtn";
    final static String headerChat = "#headerChat";
    final static String loggedInAs = "#loggedInAs";

    @Before
    public void enterRegistrationView() {
        System.out.println("Entering RegistrationView");

    }


    /**
     * Function that access UserSearch view and checks the existance of the fields.
     *
     */
    @Test
    public void CheckField(){
        UserHelper.createUser(this,"login_validUser","validPassword");
        UserHelper.loginUser(this,"login_validUser","validPassword");
        verifyThat(togglebutton,hasText("Search Users"));
        verifyThat(togglebutton,NodeMatchers.isVisible());
        clickOn(togglebutton);
        verifyThat(searchBtn, hasText("Search"));
        verifyThat(searchBtn, NodeMatchers.isEnabled());
        verifyThat(ChatBtn, hasText("Start Chat"));
        verifyThat(ChatBtn, NodeMatchers.isEnabled());
        verifyThat(usernameFld, NodeMatchers.isVisible());
        verifyThat(errormessage, NodeMatchers.isInvisible());
        verifyThat(errorusermessage,NodeMatchers.isInvisible());
        assertTrue(find(loggedInAs).isVisible());
        verifyThat(loggedInAs, NodeMatchers.hasText("Logged in as: "+"login_validUser"));

    }

    /**
     * Test CheckError message for UserSearchField
     * 1 Case the usersearchField cannot be empty
     * 2 Case the usersearchField cannot be less than 3 characters
     */
    @Test
    public void CheckErrorMessages(){
        UserHelper.loginUser(this,"login_validUser","validPassword");
        clickOn(togglebutton);
        //check when we use empty string to  user_name_field to search
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("can't be blank"));
        //check when we use two characters as user_name_field to search
        clickOn(usernameFld).write("aa");
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("should be at least 3 character(s)"));
        //check when we search for a name that does not actually exists
        clickOn(usernameFld).write("akama");
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("No User Available"));
        //check when we do not select user to start a conversation
        clickOn(ChatBtn);
        assertTrue(find(errormessage).isVisible());
        verifyThat(errormessage,NodeMatchers.hasText("No user was selected"));
    }

    /**
     * Test Check Success Search and start new conversation
     */
    @Test
    public void CheckSuccessConversationStart(){
        UserHelper.createUser(this,"login_validUser_two","validPassword_two");
        UserHelper.loginUser(this,"login_validUser_two","validPassword_two");
        clickOn(togglebutton);
        clickOn(usernameFld).write("login");
        clickOn(searchBtn);
        clickOn("login_validUser");
        clickOn(ChatBtn);
        assertTrue(find(headerChat).isVisible());
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: " +"login_validUser"));
        assertTrue(find(loggedInAs).isVisible());
        verifyThat(loggedInAs, NodeMatchers.hasText("Logged in as: "+"login_validUser_two"));


    }
}
