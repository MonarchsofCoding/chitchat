package com.moc.chitchat;

import android.support.test.espresso.core.deps.guava.util.concurrent.ThreadFactoryBuilder;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.activity.SearchUserActivity;
import com.moc.chitchat.controller.SearchUserController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchUserActivityTest{

    private String usernameTyped;
    private String passwordTyped;

    @Rule
    public ActivityTestRule<SearchUserActivity> searchUserActivityActivityRule
        = new ActivityTestRule<>(SearchUserActivity.class);

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    @Before
    public void login() throws InterruptedException {
        //loginActivityRule.getActivity();

        usernameTyped = "vjftw";
        passwordTyped = "Abc123!?";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

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


        String expectedOutput  = "You can only do a search with an input longer than " +
            "3 characters\n";
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


        String expectedOutput  = "You can only do a search with an input longer than " +
            "3 characters\n";
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

        String expectedOutput  = "Query made with query text: " + usernameTyped + "\n";
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

        String expectedOutput  = "Query made with query text: " + usernameTyped + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
