package com.moc.chitchat.view.authentication;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.UserHelper;
import com.moc.chitchat.view.main.MainViewTest;
import com.moc.chitchat.view.main.WestViewTest;
import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * LoginViewTest provides UI tests for the LoginView.
 */
public class LoginViewTest extends PrimaryStageTest {

    public static final String viewPane = "#login-view-form";
    public static final String usernameFld = "#login-username-fld";
    public static final String passwordFld = "#login-password-fld";
    public static final String loginBtn = "#login-login-btn";
    public static final String registerBtn = "#login-register-btn";
    public static final String unexpectedErrors = "#login-errors-lbl";
    public static final String credits = "#base-footer-credits";

    @Before
    public void enterLoginView() {
        System.out.println("Entering LoginView");
    }

    /**
     * Testing existence of our buttons and TextFields areas to the login view.
     */
    @Test
    public void test_login_buttons_are_enabled(){

        verifyThat(usernameFld, NodeMatchers.isVisible());
        verifyThat(passwordFld, NodeMatchers.isVisible());

        verifyThat(loginBtn, hasText("Login"));
        verifyThat(loginBtn, NodeMatchers.isEnabled());

        verifyThat(registerBtn, hasText("Register"));
        verifyThat(registerBtn, NodeMatchers.isEnabled());

        verifyThat(unexpectedErrors, NodeMatchers.isInvisible());
        verifyThat(credits,NodeMatchers.isVisible());
        verifyThat(credits,NodeMatchers.hasText("Created by: Monarchs of Coding"));
    }


    /**
    * Testing the Login for wrong credentials.
    */
    @Test
    public void test_invalid_login_credentials() throws InterruptedException {

        clickOn(usernameFld).write("login_validUse");
        clickOn(passwordFld).write("validPassword");
        clickOn(loginBtn);
        Thread.sleep(2000);
        verifyThat(unexpectedErrors, NodeMatchers.isVisible());
    }

    /**
     * Tests valid credentials take you to the main view.
     */
    @Test
    public void test_valid_login_credentials() throws InterruptedException {

        // Create a User first
        UserHelper.createUser(this,"login_validUser", "validPassword");
        // Test login with created user
        clickOn(usernameFld).write("login_validUser");
        clickOn(passwordFld).write("validPassword");
        clickOn(loginBtn);
        Thread.sleep(2000);
        verifyThat(WestViewTest.westview, NodeMatchers.isVisible());
    }


}
