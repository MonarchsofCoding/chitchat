package com.moc.chitchat;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.mockito.Mockito.mock;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CurrentChatActivityTest {

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

        String expectedOutput = "Message from " + usernameTyped + " is sent to " + usernameToSearch
            + "\n" + "The sent message: " + message + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testReceiveMessage() throws JSONException, InterruptedException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Map<String, String> requestHeaders = null;

        requestHeaders = new HashMap<String, String>();
        requestHeaders.put(
            "authorization",
            "Bearer "
                + loginActivityRule.getActivity().sessionConfiguration
                    .getCurrentUser().getAuthToken());

        final Map<String, String> finalRequestHeaders = requestHeaders;

        String message = "Hi mate!";

        MessageModel testMessage = new MessageModel(
            new UserModel(usernameTyped),
            message
        );

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            String.format("%s%s",
                loginActivityRule.getActivity().getResources().getString(R.string.server_url),
                "/api/v1/messages"
            ),
            testMessage.tojsonObject(),
            mock(Response.Listener.class),
            mock(Response.ErrorListener.class)
        ) {
            /* getHeaders Overridden method for fetching the headers.
             * @return the headers to the request.
             */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerParams = new HashMap<String, String>();
                if (finalRequestHeaders != null) {
                    for (Map.Entry<String, String> header : finalRequestHeaders.entrySet()) {
                        headerParams.put(header.getKey(), header.getValue());
                    }
                }
                return headerParams;
            }
        };
        Volley.newRequestQueue(loginActivityRule.getActivity().getBaseContext())
            .add(jsonObjectRequest);

        Thread.sleep(3000);

        String expectedOutput = "Message from " + usernameTyped + " is received.\n"
            + "The received message: " + message + "\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
