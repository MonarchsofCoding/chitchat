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
import com.moc.chitchat.activity.SearchUserActivity;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchUserActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);
    
    private String usernameTyped;
    private String passwordTyped;

    /**
     *Does login before tests to go through the register and the login activities.
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    @Before
    public void initialization() throws InterruptedException {
        register("aydinakyol");
        register("vjftw");
        login();
    }

    public void register(String usernameTyped) throws InterruptedException {
        passwordTyped = "Abc123!?";
        String passwordReTyped = "Abc123!?";

        onView(withId(R.id.register_button)).perform(click());

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.reinput_password_input))
            .perform(typeText(passwordReTyped), closeSoftKeyboard());

        onView(withId(R.id.register_button)).perform(click());

        Thread.sleep(1000);
    }

    public void login() throws InterruptedException {
        usernameTyped = "vjftw";
        passwordTyped = "Abc123!?";

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);
    }

    @Test
    public void lessThanThreeCharQuery() throws InterruptedException {
        usernameTyped = "ay";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameTyped));

        onView(withId(R.id.search_layout_button)).perform(click());


        String expectedOutput = "You can only do a search with an input longer than "
            + "3 characters\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void zeroCharQuery() throws InterruptedException {
        usernameTyped = "";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameTyped));

        onView(withId(R.id.search_layout_button)).perform(click());


        String expectedOutput = "You can only do a search with an input longer than "
            + "3 characters\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void true_ThreeCharInput() throws InterruptedException {
        usernameTyped = "ayd";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameTyped));

        onView(withId(R.id.search_layout_button)).perform(click());

        String expectedOutput = "Query made with query text: " + usernameTyped + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void true_LongInput() throws InterruptedException {
        usernameTyped = "aydinakyol";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameTyped));

        onView(withId(R.id.search_layout_button)).perform(click());

        String expectedOutput = "Query made with query text: " + usernameTyped + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
