package com.moc.chitchat.view;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.api.FxRobotException;
import org.testfx.matcher.base.NodeMatchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 * Created by spiros on 26/02/2017.
 */
public class LoginViewTest extends PrimaryStageTest {

    final String loginButton = "#loginBtn";
    final String usernamefield= "#usernameField";
    final String passwordfield= "#passwordField";
    final String registerBtn = "#registerBtn";
    final String errorslabel = "#errors";

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
        verifyThat(errorslabel,NodeMatchers.isInvisible());

    }
    /**
     * Check the writing behaviour of our fields.
      */
    @Test
    public void testTextFieldsBehaviors() {
        String username = "antreas";
        String password = "lalalala";
        clickOn(usernamefield).write(username);
        clickOn(passwordfield).write(password);


    }
    /*
    * Testing the LoginButtoa for wrong credentials.
    * case 1 no username we check it with visible and invisible label of errors.
    */
    @Test
     public void testLoginBtnNoUsername(){
        String password = "lalala";
        clickOn(passwordfield).write(password);
        clickOn(loginButton);
        assertTrue(find(errorslabel).isVisible());

    }

    /*
     * Testing the LoginButtoa for wrong credentials.
     * case 2 no password we check it with visible and invisible label of errors.
     */
    @Test
    public void testLoginBtnNoPassword(){
        String username = "spiros";
        clickOn(usernamefield).write(username);
        clickOn(loginButton);
        assertTrue(find(errorslabel).isVisible());

    }
    /*
     * Testing the LoginButtoa for wrong credentials.
     * case 2 username less than 3 characters we check it with visible and invisible label of errors.
     */
    @Test
    public void testLoginBtnUsenameLenght(){
        String username = "sp";
        clickOn(usernamefield).write(username);
        clickOn(loginButton);
        assertTrue(find(errorslabel).isVisible());

    }
    /*
    * Testing the LoginButtoa for wrong credentials.
    * case 2 password less than 8 characters we check it with visible and invisible label of errors.
    */
    @Test
    public void testLoginBtnPasswordLength(){
        String username = "sp";
        clickOn(usernamefield).write(username);
        clickOn(loginButton);
        assertTrue(find(errorslabel).isVisible());

    }







}

