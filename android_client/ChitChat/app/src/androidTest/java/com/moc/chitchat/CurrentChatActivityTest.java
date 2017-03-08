package com.moc.chitchat;

import android.support.test.espresso.core.deps.guava.base.Strings;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.moc.chitchat.activity.ChatListActivity;
import com.moc.chitchat.activity.CurrentChatActivity;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.activity.SearchUserActivity;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.controller.CurrentChatController;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dalvik.annotation.TestTargetClass;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CurrentChatActivityTest {

    @Inject
    HttpClient httpClient;

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    private String usernameTyped;
    private String passwordTyped;
    private String usernameToSearch;

    /**
     * Does login before tests to go through the login and search activity.
     *
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    @Before
    public void loginAndSearch() throws InterruptedException {

        usernameTyped = "vjftw";
        passwordTyped = "Abc123!?";
        usernameToSearch = "aydinakyol";

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameToSearch));

        onView(withId(R.id.search_layout_button)).perform(click());

        onData(hasToString(startsWith(usernameToSearch)))
            .inAdapterView(withId(R.id.users_list)).atPosition(0)
            .perform(click());

        Thread.sleep(1000);
    }

    @Test
    public void testSendMessage() {
        String message = "Hi mate!";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        onView(withId(R.id.message_text))
            .perform(typeText(message), closeSoftKeyboard());

        onView(withId(R.id.send_button)).perform(click());

        String expectedOutput = "Message from " + usernameTyped + " is sent to " + usernameToSearch +
            "\n" + "The sent message: " + message + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReceiveMessage() throws JSONException {
        String message = "Hi mate!";

        MessageModel testMessage = new MessageModel(
            new UserModel(usernameToSearch),
            message
        );

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        httpClient.sendRequest(
            loginActivityRule.getActivity(),
            Request.Method.POST,
            "/api/v1/messages",
            testMessage.tojsonObject(),
            null,
            null,
            true
        );


        String expectedOutput = "Message from " + usernameTyped + " is received.\n" +
            "The received message: " + message + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
