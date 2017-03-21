package com.moc.chitchat;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.android.volley.Response;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.helper.MessageHelper;

import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CurrentChatActivityReceiveMessageTest implements Response.Listener<JSONObject> {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
            LoginActivity.class);

    private String usernameTyped;
    private String usernameToSearch;
    private String passwordTyped;
    private String header;

    /**
     * Does login before tests to go through the register the login and search activities.
     *
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    @Before
    public void initialization() throws Exception {
        register("test3");
        register("test4");
        loginActual();
    }

    /**
     * Registration.
     *
     * @param usernameTyped username to register.
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
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

    /**
     * Login the actual user.
     *
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    public void loginActual() throws InterruptedException {
        usernameTyped = "test3";
        String passwordTyped = "Abc123!?";

        onView(withId(R.id.username_input))
                .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
                .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            header = response.getJSONObject("data").get("authToken").toString();
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    @Test
    public void testReceiveMessage() throws Exception {

        // Receive a message
        MessageHelper.sendMessage(
                "test4",
                "Abc123!?",
                "test3",
                "Hello!"
        );
        Thread.sleep(3000);

        // Switch to Chats View
        Matcher<View> matcher = allOf(withText("Chats"), isDescendantOfA(withId(R.id.menu_tabs)));
        onView(matcher).perform(click());

        // Select conversation
        onData(hasToString(startsWith("test4")))
            .inAdapterView(withId(R.id.chats_list)).atPosition(0)
            .perform(click())
        ;
        Thread.sleep(2000);

        onView(withId(R.id.message_panel)).check(matches(withText("\ntest4: Hello!")));

    }
}
