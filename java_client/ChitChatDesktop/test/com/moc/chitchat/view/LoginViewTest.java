package com.moc.chitchat.view;

import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.api.FxAssert;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * Created by spiros on 26/02/2017.
 */
public class LoginViewTest extends AuthenticationStageTest {

    final String loginButton = "#loginBtn";
    final String usernamefieldn= "#usernameField";
    final String passwordfield= "#passwordField";

//    @Test(expected = FxRobotException.class)
//    public void ClickONfalseElement(){
//        clickOn("#vjt");
//    }

    @Test
    public void testHasLoginButton(){
        verifyThat("#loginBtn", hasText("Login"));

        verifyThat(loginButton, NodeMatchers.isEnabled());
    }


}
