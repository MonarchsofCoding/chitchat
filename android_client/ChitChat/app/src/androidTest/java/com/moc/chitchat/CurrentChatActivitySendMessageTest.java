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
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.KeyPair;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CurrentChatActivitySendMessageTest{

    @Rule
    public ActivityTestRule<LoginActivity> loginClassActivityRule = new ActivityTestRule<>(
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
        register("test1");
        register("test2");
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
        usernameTyped = "test1";
        String passwordTyped = "Abc123!?";

        onView(withId(R.id.username_input))
            .perform(typeText(usernameTyped), closeSoftKeyboard());

        onView(withId(R.id.password_input))
            .perform(typeText(passwordTyped), closeSoftKeyboard());

        onView(withId(R.id.login_button)).perform(click());

        Thread.sleep(2000);
    }

    public void loginOther() throws Exception {
        usernameToSearch = "test2";
        CryptoBox cryptoBox = new CryptoBox();

        UserModel user = new UserModel(usernameToSearch);
        user.setPassword(passwordTyped);

        KeyPair keyPair = cryptoBox.generateKeyPair();
        user.setPublicKey(keyPair.getPublic());
        user.setPrivateKey(keyPair.getPrivate());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST,
            String.format("%s%s",
                loginClassActivityRule.getActivity().getResources().getString(R.string.server_url),
                "/api/v1/auth"
            ),
            user.toJsonObjectForLogin(),
            mock(Response.Listener.class),
            mock(Response.ErrorListener.class)
        );
        Volley.newRequestQueue(loginClassActivityRule.getActivity().getBaseContext())
            .add(jsonObjectRequest);
    }

    /**
     * Searching a user.
     * @throws InterruptedException throws in case the Thread.sleep(ms) fails
     */
    public void search() throws InterruptedException {
        usernameToSearch = "test2";

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
}
