package com.moc.chitchat.view.helper;

import com.moc.chitchat.view.authentication.LoginView;
import com.moc.chitchat.view.authentication.LoginViewTest;
import com.moc.chitchat.view.authentication.RegistrationViewTest;
import org.testfx.framework.junit.ApplicationTest;

/**
 * UserHelper provides functions to manage User objects in tests.
 */
public class UserHelper {

    /**
     * A helper for other ViewTests to create new users.
     * @param username The username of the new user.
     * @param password the password of the new user.
     */
    public static void createUser(ApplicationTest testContext, String username, String password) {
        testContext.clickOn(LoginViewTest.registerBtn);

        testContext.clickOn(RegistrationViewTest.usernameFld).write(username);
        testContext.clickOn(RegistrationViewTest.passwordFld).write(password);
        testContext.clickOn(RegistrationViewTest.passwordCheckFld).write(password);

        testContext.clickOn(RegistrationViewTest.registerBtn);
    }
    public static void loginUser(ApplicationTest testContext, String username, String password){

        testContext.clickOn(LoginViewTest.usernameFld).write(username);
        testContext.clickOn(LoginViewTest.passwordFld).write(password);
        testContext.clickOn(LoginViewTest.loginBtn);
    }
}
