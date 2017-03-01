package com.moc.chitchat.view;

import javafx.scene.Node;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testfx.api.FxAssert.verifyThat;

/**
 * Created by AIGERIM on 28/02/2017.
 */
public class RegistrationViewTest extends PrimaryStageTest {

    final static String loginButton = "#loginBtnreg";
    final static String usernamefield = "#usernamefieldreg";
    final static String passwordfield = "#passwordFieldreg";
    final static String passwordCheckField = "#passwordCheckField";
    final static String registerBtn = "#registerBtn";
    final static String registerBtnreg = "#registerBtnreg";
    final static String servererrors = "#servererrorsreg";
    final static String passwordErrors = "#passwordErrorsreg";
    final static String passwordCheckErrors = "#passwordCheckErrors";
    final static String usernameErrors = "#usernameErrorsreg";

    /**
     * Check the writing behaviour of our fields.
     */
    @Test
    public void testUITextFieldsBehaviors() {

        clickOn(registerBtn).clickOn(usernamefield).write("Aika").clickOn(registerBtnreg);
        assertTrue(find(servererrors).isVisible());
        assertFalse(find(usernameErrors).isVisible());
        assertTrue(find(passwordErrors).isVisible());


    }

    @Test
    public void testPasswordNotEnoughChar() {
        clickOn(registerBtn).clickOn(usernamefield).write("Aika");
        clickOn(passwordfield).write("aaaaaaa");
        clickOn(passwordCheckField).write("aaaaaaa");
        clickOn(registerBtnreg);
        assertTrue(find(servererrors).isVisible());
        assertFalse(find(usernameErrors).isVisible());
        assertFalse(find(passwordErrors).isVisible());
    }

    @Test
    public void testPasswordDoesNotMatch() {
        clickOn(registerBtn).clickOn(usernamefield).write("Aika");
        clickOn(passwordfield).write("aaaaaaaa");
        clickOn(passwordCheckField).write("aaabbbcc");
        clickOn(registerBtnreg);
        assertTrue(find(servererrors).isVisible());
        assertFalse(find(usernameErrors).isVisible());

    }

    @Test
    public void testUsernameEmpty() {
        clickOn(registerBtn).clickOn(usernamefield).write("");
        clickOn(passwordfield).write("aaaaaaaa");
        clickOn(passwordCheckField).write("aaaaaaaa");
        clickOn(registerBtnreg);
        assertTrue(find(servererrors).isVisible());
        assertTrue(find(usernameErrors).isVisible());
        assertFalse(find(passwordErrors).isVisible());
    }

    @Test
    public void testValidRegistration() throws InterruptedException {
        clickOn(registerBtn).clickOn(usernamefield).write("Aika");
        clickOn(passwordfield).write("aaaaaaaa");
        clickOn(passwordCheckField).write("aaaaaaaa");
        clickOn(registerBtnreg);

        Thread.sleep(1000);

        // Then it should show the Login view
        verifyThat(LoginViewTest.loginButton, Node::isVisible);
        verifyThat(LoginViewTest.usernamefield, Node::isVisible);
        verifyThat(LoginViewTest.passwordfield, Node::isVisible);
        verifyThat(LoginViewTest.registerBtn, Node::isVisible);
    }

    @Test
    public void testThatUserCanReturnToLoginFromRegister() {
        clickOn(registerBtn).clickOn(usernamefield).write("Aika");
        clickOn(passwordfield).write("aaaaaaaa");
        clickOn(loginButton);

        // Then it should show the Login view
        verifyThat(LoginViewTest.loginButton, Node::isVisible);
        verifyThat(LoginViewTest.usernamefield, Node::isVisible);
        verifyThat(LoginViewTest.passwordfield, Node::isVisible);
        verifyThat(LoginViewTest.registerBtn, Node::isVisible);
    }
}
