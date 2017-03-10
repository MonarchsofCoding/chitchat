package com.moc.chitchat.view.authentication;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.authentication.LoginViewTest;
import javafx.scene.Node;
import org.junit.Before;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;

/**
 * RegistrationViewTest provides UI tests for the Registration View.
 */
public class RegistrationViewTest extends PrimaryStageTest {

    public static final String usernameFld = "#register-username-fld";
    public static final String usernameErrs = "#register-username-errs";

    public static final String passwordFld = "#register-password-fld";
    public static final String passwordErrs = "#register-password-errs";

    public static final String passwordCheckFld = "#register-passwordCheck-fld";
    public static final String passwordCheckErrs = "#register-passwordCheck-errs";

    public static final String registerBtn = "#register-register-btn";

    public static final String unexpectedErrors = "#register-errors-lbl";

    @Before
    public void enterRegistrationView() {
        System.out.println("Entering RegistrationView");
        clickOn(LoginViewTest.registerBtn);
    }

    /**
     * Tests that the Registration TextFields are visible.
     */
    @Test
    public void test_registration_fields_are_visible() {
        verifyThat(usernameFld, Node::isVisible);
        verifyThat(passwordFld, Node::isVisible);
        verifyThat(passwordCheckFld, Node::isVisible);
    }

    /**
     * Tests that the Registration ErrorLabels are hidden by default.
     */
    @Test
    public void test_registration_error_labels_are_invisible_by_default() {
        verifyThat(unexpectedErrors, NodeMatchers.isInvisible());
        verifyThat(usernameErrs, NodeMatchers.isInvisible());
        verifyThat(passwordErrs, NodeMatchers.isInvisible());
        verifyThat(passwordCheckErrs, NodeMatchers.isInvisible());
    }

    /**
     * Tests that username is required.
     */
    @Test
    public void test_username_errors_should_be_visible_when_there_is_an_invalid_username() {
        clickOn(registerBtn);
        verifyThat(usernameErrs, NodeMatchers.isVisible());
    }

    /**
     * Tests that password is required.
     */
    @Test
    public void test_password_errors_should_be_visible_when_there_is_an_invalid_password() {
        clickOn(usernameFld).write("Aika");
        clickOn(registerBtn);

        verifyThat(usernameErrs, NodeMatchers.isInvisible());
        verifyThat(passwordErrs, NodeMatchers.isVisible());
    }

    /**
     * Tests the password check field.
     */
    @Test
    public void test_passwordCheck_errors_should_be_visible_when_the_passwords_mismatch() {
        clickOn(usernameFld).write("Aika");
        clickOn(passwordFld).write("12345678");
        clickOn(passwordCheckFld).write("12345679");
        clickOn(registerBtn);

        verifyThat(usernameErrs, NodeMatchers.isInvisible());
        verifyThat(passwordErrs, NodeMatchers.isInvisible());
        verifyThat(passwordCheckErrs, NodeMatchers.isVisible());
    }

    /**
     * Tests a valid set of registration details
     * @throws InterruptedException if the sleep is interrupted.
     */
    @Test
    public void test_valid_registration() throws InterruptedException {
        clickOn(usernameFld).write("register_validUser");
        clickOn(passwordFld).write("register_validPassword");
        clickOn(passwordCheckFld).write("register_validPassword");

        clickOn(registerBtn);

        // Automatically returns to LoginView when registration is successful
        Thread.sleep(1000);
        verifyThat(LoginViewTest.viewPane, NodeMatchers.isVisible());
    }

    /**
     * Tests that usernames should be unique.
     * @throws InterruptedException if the sleep is interrupted.
     */
    @Test
    public void test_user_cannot_register_duplicate_user() throws InterruptedException {
        clickOn(usernameFld).write("register_duplicateUser");
        clickOn(passwordFld).write("register_validPassword");
        clickOn(passwordCheckFld).write("register_validPassword");
        clickOn(registerBtn);

        clickOn(LoginViewTest.registerBtn);

        clickOn(usernameFld).write("register_duplicateUser");
        clickOn(passwordFld).write("register_validPassword");
        clickOn(passwordCheckFld).write("register_validPassword");
        clickOn(registerBtn);

        Thread.sleep(1000);

        verifyThat(usernameErrs, NodeMatchers.isVisible());
    }

}
