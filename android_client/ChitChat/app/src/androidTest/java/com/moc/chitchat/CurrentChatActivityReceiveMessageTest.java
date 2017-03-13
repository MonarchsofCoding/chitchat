package com.moc.chitchat;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
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

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.core.deps.guava.collect.Iterables;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moc.chitchat.activity.LoginActivity;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
    private String passwordTyped;
    private String usernameToSearch;
    private String url;

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

        url = loginActivityRule.getActivity().getResources().getString(R.string.server_url);

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
    public void testReceiveMessage() throws JSONException, InterruptedException {
        UserModel user = new UserModel(usernameToSearch);
        user.setPassword(passwordTyped);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            String.format("%s%s",
                url,

                "/api/v1/auth"
            ),
            user.toJsonObject(),
            this,
            mock(Response.ErrorListener.class)
        );
        Volley.newRequestQueue(loginActivityRule.getActivity().getBaseContext())
            .add(jsonObjectRequest);

        Thread.sleep(3000);
    }

    @Override
    public void onResponse(JSONObject response) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Map<String, String> requestHeaders = null;

        requestHeaders = new HashMap<String, String>();
        try {
            requestHeaders.put(
                "authorization",
                "Bearer "
                    + response.getJSONObject("data").get("authToken").toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        final Map<String, String> finalRequestHeaders = requestHeaders;

        String message = "Hi mate!";

        MessageModel testMessage = new MessageModel(
            new UserModel(usernameToSearch),
            new UserModel(usernameTyped),
            message
        );

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject
                .put("recipient", testMessage.getTo().getUsername())
                .put("message", testMessage.getMessage());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        try {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s%s",
                    url,
                    "/api/v1/messages"
                ),
                jsonObject,
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

            Collection<Activity> activities = ActivityLifecycleMonitorRegistry
                .getInstance().getActivitiesInStage(Stage.RESUMED);
            Activity currentActivity = Iterables.getOnlyElement(activities);

            Volley.newRequestQueue(currentActivity).add(jsonObjectRequest);

            Thread.sleep(5000);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String expectedOutput = "Message from " + usernameToSearch + " is received.\n"
            + "The received message: " + message + "\n";
        assertEquals(expectedOutput, outContent.toString());

    }
}
