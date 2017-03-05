package com.moc.chitchat.view.main;

import com.moc.chitchat.view.PrimaryStageTest;
import javafx.scene.input.KeyCode;
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
    final static String loginButton = "#loginBtn";
    final static String usernamefield= "#usernameField";
    final static String passwordfield= "#passwordField";

    final static String usernameFieldsearch = "#usernameFieldSearch";
    final static String searchBtn = "#searchBtn";
    final static String startConversationBtn = "#StartChatBtn";
    final static String togglebutton ="#ToggleBtn";

    final static String headerChat = "#headerChat";
    final static String newmessageField = "#newmessageField";
    final static String errormessage = "#errormessage";
    final static String sendBtn = "#sendBtnmsg";

    final static String registerBtn = "#registerBtn";
    final static String registerBtnreg = "#registerBtnreg";
    final static String usernamefieldReg = "#usernamefieldreg";
    final static String passwordfieldReg = "#passwordFieldreg";
    final static String passwordCheckField = "#passwordCheckField";


    /**
     * This is a precondition function, so we register users that we need for testing.
     */
    public void preCondition(String username) {
        String password = "ccccccccc";

        clickOn(registerBtn).clickOn(usernamefieldReg).write(username);
        clickOn(passwordfieldReg).write(password);
        clickOn(passwordCheckField).write(password);
        clickOn(registerBtnreg);
    }

    /**
     * Function access that helps us to login in ordet o access the next level.
     *
     */
    public void access_search_view2(){
        String username = "Phillip";
        String password = "ccccccccc";
        String check = "Leonardo";

        clickOn(usernamefield).write(username);
        clickOn(passwordfield).write(password);
        clickOn(loginButton);
        clickOn(togglebutton);
        clickOn(usernameFieldsearch).write("Leo");
        clickOn(searchBtn);
        clickOn(check);
        clickOn(startConversationBtn);
    }
    /**
     *Test The existance of main parts of the field
     */
    @Test
    public void CheckFields(){
        preCondition("Phillip");
        preCondition("Leonardo");
        access_search_view2();
        String check = "Leonardo";
        verifyThat(togglebutton,hasText("Search Users"));
        verifyThat(togglebutton, NodeMatchers.isVisible());
        verifyThat(newmessageField, NodeMatchers.isEnabled());
        verifyThat(check, NodeMatchers.isVisible());
        assertTrue(find(headerChat).isVisible());
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check));
        verifyThat(errormessage, NodeMatchers.isInvisible());
        assertTrue(find(sendBtn).isVisible());
        }



    /**
     *Test the sending message function works
     */
    @Test
    public void CheckSendingMessagesFunction(){
        access_search_view2();
        String messagetest = "hello";
        String messagedisplay = "Phillip: " +
                ""+messagetest;
        clickOn(newmessageField).write(messagetest).clickOn(sendBtn);
        clickOn(messagedisplay);
        verifyThat(messagedisplay,NodeMatchers.isVisible());


    }
    /**
     *Test the sending message function works
     */
    @Test
    public void CheckSendingEmptyMessagesFunction(){
        access_search_view2();
        clickOn(newmessageField).clickOn(sendBtn);
        verifyThat(errormessage,NodeMatchers.isVisible());

    }
}
