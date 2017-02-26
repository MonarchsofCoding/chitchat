package com.moc.chitchat.view;

import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by spiros on 26/02/2017.
 */
public class LoginViewTest extends AuthenticationStageTest {

    final String Login_Button= "#loginBtn";
    final String usernamefieldn= "#usernamefield";
    final String passwordfield= "#passwordfield";

    @Test(expected = FxRobotException.class)
    public void ClickONfalseElement(){
        clickOn("#vjt");
    }

    @Test
    public void HasLoginButton(){
        clickOn(Login_Button);
        verifyThat(Login_Button, NodeMatchers.isEnabled());
    }


}
