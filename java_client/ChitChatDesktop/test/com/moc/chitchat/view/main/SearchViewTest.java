package com.moc.chitchat.view.main;

import com.moc.chitchat.view.PrimaryStageTest;
import com.moc.chitchat.view.helper.UserHelper;
import javafx.scene.input.KeyCode;
import org.junit.Test;
import org.testfx.matcher.base.NodeMatchers;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

/**
 * SearchViewTest provides tests for the search view functionality.
 */
public class SearchViewTest extends PrimaryStageTest {

    public final static String usernameFld = "#search-username-fld";
    public final static String userList = "#search-user-list";
    public final static String searchBtn = "#search-btn";
    public final static String chatBtn = "#search-chat-btn";
    public final static String errorMessage = "#search-error-messages";
    public final static String errorUserMessage ="#search-error-users-msg";
    public final static String logoutBtn = "#logout-button";
    public static final String viewPane = "#login-view-pane";
    public static final String usernameloginFld = "#login-username-fld";
    public static final String passwordFld = "#login-password-fld";
    public static final String loginBtn = "#login-login-btn";
    public static final String registerBtn = "#login-register-btn";
    public static final String unexpectedErrors = "#login-errors-lbl";
    public static final String credits = "#credits";
    /**
     * Test that the search view is visible.
     */
    @Test
    public void test_search_view_is_visible() {
        UserHelper.createUser(this, "search_user_visible", "search1234");
        UserHelper.loginUser(this, "search_user_visible", "search1234");
        WestViewTest.enterSearchView(this);

        verifyThat(searchBtn, NodeMatchers.isEnabled());
        verifyThat(searchBtn, NodeMatchers.isVisible());
        verifyThat(searchBtn, hasText("Search"));

        verifyThat(chatBtn, NodeMatchers.isEnabled());
        verifyThat(chatBtn, NodeMatchers.isVisible());
        verifyThat(chatBtn, hasText("Start Chat"));

        verifyThat(usernameFld, NodeMatchers.isVisible());
        verifyThat(usernameFld, NodeMatchers.isEnabled());
        verifyThat(usernameFld, NodeMatchers.hasText(""));

        verifyThat(errorMessage, NodeMatchers.isInvisible());
        verifyThat(errorUserMessage,NodeMatchers.isInvisible());
        verifyThat(logoutBtn ,NodeMatchers.isVisible());
    }

    /**
     * Tests for username validation.
     */
    @Test
    public void test_errors_username_validation() {
        UserHelper.createUser(this, "search_validation", "search1234");
        UserHelper.loginUser(this, "search_validation", "search1234");
        WestViewTest.enterSearchView(this);

        clickOn(searchBtn);
        verifyThat(errorUserMessage, NodeMatchers.isVisible());
        verifyThat(errorUserMessage, NodeMatchers.hasText("can't be blank"));

        clickOn(usernameFld).write("sp");
        clickOn(searchBtn);
        verifyThat(errorUserMessage, NodeMatchers.isVisible());
        verifyThat(errorUserMessage, NodeMatchers.hasText("should be at least 3 character(s)"));
        clickOn(usernameFld).push(KeyCode.BACK_SPACE).push(KeyCode.BACK_SPACE);

        clickOn(usernameFld).write("register_nonexistent_user");
        clickOn(searchBtn);
        verifyThat(errorUserMessage, NodeMatchers.isVisible());
        verifyThat(errorUserMessage, NodeMatchers.hasText("No User Available"));
    }

    /**
     * Tests that users are returned in the search view list.
     */
    @Test
    public void test_search_users_found_list() {
        // Create dummy users
        UserHelper.createUser(this, "search_user1", "search1234");
        UserHelper.createUser(this, "search_user2", "search1234");
        UserHelper.createUser(this, "search_user3", "search1234");
        UserHelper.createUser(this, "search_user4", "search1234");
        UserHelper.createUser(this, "search_user5", "search1234");

        UserHelper.createUser(this, "search_user_find", "search1234");
        UserHelper.loginUser(this, "search_user_find", "search1234");
        WestViewTest.enterSearchView(this);

        clickOn(usernameFld).write("search_user");
        clickOn(searchBtn);

        verifyThat(userList, NodeMatchers.anything());
    }


    /**
     * We test the logout button that returns the user to the login screen.
     */
    @Test
    public void test_logout_button(){
        UserHelper.createUser(this, "search_user6", "search1234");
        UserHelper.loginUser(this, "search_user6", "search1234");
        clickOn(logoutBtn);
        verifyThat(usernameloginFld, NodeMatchers.isVisible());
        verifyThat(passwordFld, NodeMatchers.isVisible());

        verifyThat(loginBtn, hasText("Login"));
        verifyThat(loginBtn, NodeMatchers.isEnabled());

        verifyThat(registerBtn, hasText("Register"));
        verifyThat(registerBtn, NodeMatchers.isEnabled());

        verifyThat(unexpectedErrors, NodeMatchers.isInvisible());
        verifyThat(credits,NodeMatchers.isVisible());
        verifyThat(credits,NodeMatchers.hasText("Created by: Monarchs of Coding"));

    }

}
