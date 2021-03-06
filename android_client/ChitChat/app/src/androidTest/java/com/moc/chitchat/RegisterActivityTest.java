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

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    private String usernameTyped;
    private String passwordTyped;
    private String passwordReTyped;

    @Before
    public void registrationInit() {
        onView(withId(R.id.register_button)).perform(click());
    }

    @Test
    public void true_Input() throws InterruptedException {

        usernameTyped = "test0";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        Thread.sleep(1000);

        String expectedOutput = String.format("Successfully registered: %s\n", usernameTyped);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void empty_userInput() throws InterruptedException {

        usernameTyped = "";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        String expectedOutput = "Username: [Username cannot be empty.]\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void empty_passInput() throws InterruptedException {

        usernameTyped = "test0";
        passwordTyped = "Abc123!?";
        passwordReTyped = "";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        String expectedOutput = "Password Check: [The passwords must match,]\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void false_passMatch() throws InterruptedException {

        usernameTyped = "test0";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Def123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        String expectedOutput = "Password Check: [The passwords must match,]\n";
        assertEquals(expectedOutput, outContent.toString());

    }

    @Test
    public void false_passInput() throws InterruptedException {

        usernameTyped = "test0";
        passwordTyped = "Abc123";
        passwordReTyped = "Abc123";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        Thread.sleep(2000);

        String expectedOutput = "Password: [\"should be at least 8 character(s)\"]\n";
        assertEquals(expectedOutput, outContent.toString());

    }
}
