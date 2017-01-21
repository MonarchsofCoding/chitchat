package com.moc.chitchat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

/**
 * Created by aakyo on 21/01/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private String usernameTyped;
    private String passwordTyped;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    @Test
    public void true_Input() {

        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        String expectedOutput  = "OK.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }

    @Test
    public void empty_userInput() {
        usernameTyped = "";
        passwordTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        String expectedOutput  = "ERROR: the username cannot be empty.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }

    @Test
    public void empty_passInput() {
        usernameTyped = "aydinakyol";
        passwordTyped = "";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        String expectedOutput  = "ERROR: the password cannot be empty.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }

    @Test
    public void false_passInput() {
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        String expectedOutput  = "ERROR: the password does not match with the desired password" +
            " pattern.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }
}

