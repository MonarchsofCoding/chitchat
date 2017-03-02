package com.moc.chitchat.view;

import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.main.WestView;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * Created by spiros on 02/03/2017.
 */
public class SearchViewTest extends PrimaryStageTest {

    final static String loginButton = "#loginBtn";
    final static String usernamefield= "#usernameField";
    final static String passwordfield= "#passwordField";

    final static String usernameFieldsearch = "#usernameFieldSearch";
    final static String searchBtn = "#searchBtn";
    final static String startConversationBtn = "#StartChatBtn";
    final static String errormessage = "#errormessageSearch";
    final static String errorusermessage ="#errorusermsgsearch";
    final static String togglebutton ="#ToggleBtn";
    final static String headerChat = "#headerChat";

    /**
     * Function access that helps us to login in ordet o access the next level.
     *
     */
    public void access_search_view(){
        String username = "spiros";
        String password = "aaaaaaaa";

        clickOn(usernamefield).write(username);
        clickOn(passwordfield).write(password);
        clickOn(loginButton);


    }
    /**
     *Test The existance of main parts of the field
     */
    @Test
    public void CheckField(){
        access_search_view();
        verifyThat(togglebutton,hasText("Search Users"));
        verifyThat(togglebutton,NodeMatchers.isVisible());
        clickOn(togglebutton);
        verifyThat(searchBtn, hasText("Search"));
        verifyThat(searchBtn, NodeMatchers.isEnabled());
        verifyThat(startConversationBtn, hasText("Start Chat"));
        verifyThat(startConversationBtn, NodeMatchers.isEnabled());
        verifyThat(usernameFieldsearch, NodeMatchers.isVisible());
        verifyThat(errormessage, NodeMatchers.isInvisible());
        verifyThat(errorusermessage,NodeMatchers.isInvisible());
    }
    /**
     * Test CheckError message for UserSearchField
     * 1 Case the usersearchField cannot be empty
     * 2 Case the usersearchField cannot be less than 3 characters
     */
    @Test
    public void CheckUserNamefieldisEmpty(){
        access_search_view();
        clickOn(togglebutton);
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("can't be blank"));

        clickOn(usernameFieldsearch).write("aa");
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("should be at least 3 character(s)"));

    }

    /**
     * Test CheckError message for UserList when user is not Available
     */
    @Test
    public void CheckNoUserSearchFound(){
        access_search_view();
        clickOn(togglebutton);
        clickOn(usernameFieldsearch).write("akama");
        clickOn(searchBtn);
        assertTrue(find(errorusermessage).isVisible());
        verifyThat(errorusermessage,NodeMatchers.hasText("No User Available"));

    }
    /**
     * Test CheckError message when you try to start a conversation with a user that does not exist
     */
    @Test
    public void CheckWrongChatStart(){
        access_search_view();
        clickOn(togglebutton);
        clickOn(startConversationBtn);
        assertTrue(find(errormessage).isVisible());
        verifyThat(errormessage,NodeMatchers.hasText("No user was selected"));

    }
    /**
     * Test Check Success Search and start new conversation
     */
    @Test
    public void CheckSuccessConversationStart(){
        access_search_view();
        String check = "spirokas";
        clickOn(togglebutton);
        clickOn(usernameFieldsearch).write("spi");
        clickOn(searchBtn);
        clickOn(check);
        clickOn(startConversationBtn);
        assertTrue(find(headerChat).isVisible());
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check));


    }
}
