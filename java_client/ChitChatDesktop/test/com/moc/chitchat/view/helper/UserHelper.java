package com.moc.chitchat.view.helper;

import com.moc.chitchat.view.authentication.LoginViewTest;
import com.moc.chitchat.view.authentication.RegistrationViewTest;
import org.testfx.framework.junit.ApplicationTest;

/**
 * UserHelper provides functions to manage User objects in tests.
 */
public class UserHelper {

    private ApplicationTest testContext;

    public UserHelper(ApplicationTest testContext) {
        this.testContext = testContext;
    }

    /**
     * A helper for other ViewTests to create new users.
     * @param username The username of the new user.
     * @param password the password of the new user.
     */
    public void createUser(String username, String password) {
        this.testContext.clickOn(LoginViewTest.registerBtn);

        this.testContext.clickOn(RegistrationViewTest.usernameFld).write(username);
        this.testContext.clickOn(RegistrationViewTest.passwordFld).write(password);
        this.testContext.clickOn(RegistrationViewTest.passwordCheckFld).write(password);

        this.testContext.clickOn(RegistrationViewTest.registerBtn);
    }
}
