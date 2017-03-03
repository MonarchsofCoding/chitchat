package com.moc.chitchat;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.moc.chitchat.activity.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);
    private String usernameTyped;
    private String passwordTyped;

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

        String expectedOutput = "Error on login: Invalid credentials or you didn't "
            + "registered yet\n";
        assertEquals(expectedOutput, outContent.toString());
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

        String expectedOutput = "Error on login: Invalid credentials or you didn't "
            + "registered yet\n";
        assertEquals(expectedOutput, outContent.toString());
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

        String expectedOutput = "Error on login: Invalid credentials or you didn't "
            + "registered yet\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void true_Input() throws InterruptedException {

        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(1000);

        String expectedOutput = String.format("Successfully logged in: %s", usernameTyped) + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
