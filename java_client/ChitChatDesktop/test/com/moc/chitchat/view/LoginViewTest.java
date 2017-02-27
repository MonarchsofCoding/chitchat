package com.moc.chitchat.view;

import javafx.scene.control.Alert;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * Created by spiros on 26/02/2017.
 */
public class LoginViewTest extends PrimaryStageTest {

    final String loginButton = "#loginBtn";
    final String usernamefield= "#usernameField";
    final String passwordfield= "#passwordField";
    final String registerBtn = "#registerBtn";

    /**
     * Testing that an element does not exist on our loginview.
     */
    @Test(expected = FxRobotException.class)
    public void ClickONfalseElement(){
        clickOn("#vjt");
    }

    /**
     * Testing existance of our buttons and TextFields areas to the loginview.
     */
    @Test
    public void testEnabledButtonAndFields(){
        verifyThat(loginButton, hasText("Login"));
        verifyThat(loginButton, NodeMatchers.isEnabled());
        verifyThat(registerBtn, hasText("Register"));
        verifyThat(registerBtn, NodeMatchers.isEnabled());
        verifyThat(usernamefield, NodeMatchers.isVisible());
        verifyThat(passwordfield, NodeMatchers.isVisible());

    }
    /**
     * Check the writing behaviour of our fields.
      */
    @Test
    public void testTextFieldsBehaviors() {
        String username = "antreas";
        String password = "lalalala";
        clickOn(usernamefield).write(username);
        assertEquals(username, "antreas");
        clickOn(passwordfield).write(password);
        assertEquals(password, "lalalala");

        clickOn(loginButton);


    }
}

