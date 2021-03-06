package com.moc.chitchat;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.mockito.Mockito.mock;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moc.chitchat.activity.CurrentChatActivity;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.UserModel;

import java.security.KeyPair;
import java.util.Collection;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatListActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    private String usernameTyped;
    private String passwordTyped;
    private String usernameToSearch;

    /**
     * Does login before tests to go through the register the login and search activities.
     *
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    @Before
    public void initialization() throws Exception {
        register("test5");
        register("test6");
        loginOther();
        loginActual();
        search();
    }

    /**
     * Registration.
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
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    public void loginActual() throws InterruptedException {
        usernameTyped = "test5";
        String passwordTyped = "Abc123!?";

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);
    }

    /**
     * Login another user.
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    public void loginOther() throws Exception {
        usernameToSearch = "test6";
        CryptoBox cryptoBox = new CryptoBox();

        UserModel user = new UserModel(usernameToSearch);
        user.setPassword(passwordTyped);

        KeyPair keyPair = cryptoBox.generateKeyPair();
        user.setPublicKey(keyPair.getPublic());
        user.setPrivateKey(keyPair.getPrivate());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            String.format("%s%s",
                loginActivityRule.getActivity().getResources().getString(R.string.server_url),
                "/api/v1/auth"
            ),
            user.toJsonObjectForLogin(),
            mock(Response.Listener.class),
            mock(Response.ErrorListener.class)
        );
        Volley.newRequestQueue(loginActivityRule.getActivity().getBaseContext())
            .add(jsonObjectRequest);
    }

    /**
     * Searching a user.
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    public void search() throws InterruptedException {
        usernameToSearch = "test6";

        onView(withId(R.id.search_layout_text)).perform(click());

        onView(withId(R.id.search_layout_text)).perform(typeText(usernameToSearch));

        onView(withId(R.id.search_layout_button)).perform(click());

        onData(hasToString(startsWith(usernameToSearch)))
            .inAdapterView(withId(R.id.users_list)).atPosition(0)
            .perform(click());

        Thread.sleep(1000);
    }

    @Test
    public void testConversationItem() throws InterruptedException {
        Matcher<View> matcher = allOf(withText("Chats"), isDescendantOfA(withId(R.id.menu_tabs)));
        onView(matcher).perform(click());

        onData(hasToString(startsWith(usernameToSearch)))
            .check(matches(withText(startsWith(usernameToSearch))));

        onData(hasToString(startsWith(usernameToSearch))).perform(click());

        Thread.sleep(2000);

        Activity currentActivity = getCurrentActivity();
        assertEquals("activity.CurrentChatActivity",currentActivity.getLocalClassName());
        assertEquals(
            usernameToSearch,
            ((CurrentChatActivity) currentActivity).getSupportActionBar().getTitle()
        );

    }

    /**
     * Get current running activity.
     * @return Current activity object.
     */
    public Activity getCurrentActivity(){
        final Activity[] currentActivity = new Activity[1];
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                    .getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }
}
