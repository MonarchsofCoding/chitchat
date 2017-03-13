package com.moc.chitchat;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.view.View;

import com.moc.chitchat.activity.CurrentChatActivity;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.model.UserModel;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatListActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityRule = new ActivityTestRule<>(
        LoginActivity.class);

    private String usernameTyped;
    private String passwordTyped;
    private String usernameToSearch;
    private String url;

    /**
     * Does login, search and send message before tests to go through the login and search activity.
     *
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    @Before
    public void initialize() throws InterruptedException {
        login();
        search();
    }

    public void login() throws InterruptedException {
        usernameTyped = "vjftw";
        passwordTyped = "Abc123!?";
        usernameToSearch = "aydinakyol";

        url = loginActivityRule.getActivity().getResources().getString(R.string.server_url);

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);
    }

    public void search() throws InterruptedException {
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
