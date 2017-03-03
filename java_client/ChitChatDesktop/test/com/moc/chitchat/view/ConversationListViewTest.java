package com.moc.chitchat.view;

import javafx.scene.Node;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by spiros on 03/03/2017.
 */
public class ConversationListViewTest extends PrimaryStageTest {


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
    String username = "spiros";
    String password = "aaaaaaaa";
    String check = "spirokas";
    String check2 = "spirokousa";
    /**
     * Function access that helps us to login in ordet o access the next level.
     *
     */
    public void access_search_view3(){

        clickOn(usernamefield).write(username);
        clickOn(passwordfield).write(password);
        clickOn(loginButton);
        clickOn(togglebutton);
        clickOn(usernameFieldsearch).write("spir");
        clickOn(searchBtn);
        clickOn(check);
        clickOn(startConversationBtn);
    }

    /**
     * Check that the Listview Button Exists
     */
    @Test
    public void check_ConversationFields(){

        clickOn(usernamefield).write(username);
        clickOn(passwordfield).write(password);
        clickOn(loginButton);
        clickOn(togglebutton);
        verifyThat(togglebutton, NodeMatchers.hasText("Conversations"));
        clickOn(togglebutton);

    }
    /**
     * Test the ConversationListViewItems
     * Case start conversation with username spirokas and send the message hello
     * Case start conversation with username spirokousa and send the message hi
     * Check the different headers and the different messages between conversations
     */

    @Test
    public void TestConversationListViewFunctionality(){
        access_search_view3();
        clickOn(newmessageField).write("hello").clickOn(sendBtn);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check));
        verifyThat(togglebutton,NodeMatchers.hasText("Search Users"));
        clickOn(togglebutton);
        clickOn(check2).clickOn(startConversationBtn).clickOn(newmessageField).write("hi").clickOn(sendBtn);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check2));
        verifyThat(togglebutton, NodeMatchers.hasText("Search Users"));
        clickOn(check);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check));
        clickOn("spiros: hello");
        clickOn(check2);
        verifyThat(headerChat,NodeMatchers.hasText("Chat with: "+check2));
        clickOn("spiros: hi");

    }


}
