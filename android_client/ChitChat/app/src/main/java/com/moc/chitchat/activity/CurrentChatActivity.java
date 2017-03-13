package com.moc.chitchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.controller.CurrentChatController;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * CurrentChatActivity provides the View and Actions involved with searching a User.
 */

public class CurrentChatActivity extends AppCompatActivity
    implements View.OnClickListener,
    ChitChatMessagesConfiguration.MessageConfigurationListener,
    Runnable,
    TabLayout.OnTabSelectedListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    TabLayout menuTabs;
    TextView messagePanel;
    TextView messageText;
    Button sendButton;

    UserModel currentReceiver;
    String currentReceiverUsername;
    ConversationModel currentConversation;
    InputMethodManager keyboardManager;

    private MessageModel currentMessage;

    @Inject
    CurrentChatController currentChatController;
    @Inject
    SessionConfiguration sessionConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;
    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    ErrorResponseResolver errorResponseResolver;
    @Inject
    CryptoBox cryptoBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        keyboardManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        currentReceiver = currentChatConfiguration.getCurrentRecipient();

        currentReceiverUsername = currentReceiver.getUsername();

        currentConversation = chitChatMessagesConfiguration.getConversation(currentReceiver);

        this.setContentView(R.layout.activity_current_chat);
        getSupportActionBar().setTitle(currentReceiverUsername);

        messageText = (TextView) findViewById(R.id.message_text);

        currentMessage = new MessageModel(
            sessionConfiguration.getCurrentUser(),
            null,
            ""
        );

        messagePanel = (TextView) findViewById(R.id.message_panel);
        messagePanel.setMovementMethod(new ScrollingMovementMethod());
        getMessagesToDisplay();

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(2);
        tab.select();
        menuTabs.addOnTabSelectedListener(this);

        chitChatMessagesConfiguration.setMessageConfigurationListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //For when a button is clicked
    @Override
    public void onClick(View view) {
        if (!messageText.getText().toString().equals("")) {
            keyboardManager.hideSoftInputFromWindow(messageText.getWindowToken(), 0);
            try {
                currentMessage.setTo(currentReceiver);
                currentMessage.setMessage(messageText.getText().toString());
                currentChatController.sendMessageToRecipient(
                    this,
                    this,
                    this,
                    new MessageModel(
                        currentReceiver,
                        cryptoBox.encrypt(
                            messageText.getText().toString(),
                            currentReceiver.getPublicKey())
                    )
                );
            } catch (JSONException jsonexception) {
                jsonexception.printStackTrace();
                Toast.makeText(this,
                    "Error caused by JSONObject: " + jsonexception.getMessage(),
                    Toast.LENGTH_LONG);
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(this,
                    "Error caused by Encryption System: " + ex.getMessage(),
                    Toast.LENGTH_LONG);
            }
        }
    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            JSONObject response = this.errorResponseResolver.getResponseBody(error);

            JSONObject responseErrors = response.getJSONObject("errors");

            if (responseErrors.has("recipient")) {
                JSONArray recipientErrors = responseErrors.getJSONArray("recipient");
                Toast.makeText(this,
                    String.format("Recipient: %s", recipientErrors.toString()),
                    Toast.LENGTH_LONG).show();
            }

            if (responseErrors.has("message")) {
                JSONArray messageErrors = responseErrors.getJSONArray("message");
                Toast.makeText(this,
                    String.format("Message: %s", messageErrors.toString()),
                    Toast.LENGTH_LONG).show();
            }
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            String from = response.getJSONObject("data").get("sender").toString();

            UserModel fromUser = new UserModel(from);
            UserModel toUser = currentMessage.getTo();

            chitChatMessagesConfiguration.addMessageToConversation(
                toUser,
                new MessageModel(fromUser, toUser, currentMessage.getMessage()),
                true
            );
            addMessageToPanel(from, currentMessage.getMessage(), true);
            System.out.println("Message from " + fromUser.getUsername() + " is sent to "
                + toUser.getUsername());
            System.out.println("The sent message: " + currentMessage.getMessage());
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
            Toast.makeText(this,
                "Error caused by JSONObject: " + jsonexception.getMessage(), Toast.LENGTH_LONG);
        }
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();
        if (tabName.equals("Chats")) {
            launchActivity(ChatListActivity.class);
        } else if (tabName.equals("Search Users")) {
            launchActivity(SearchUserActivity.class);
        }
    }

    //For a unselected tab
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //Basically do nothing, at the moment at least.
    }

    //For a re-selected tab.
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        System.out.println("Tab: " + tab.getText().toString() + " is reselected.");
    }

    /**
     * To add a message to the messagePanel with the username.
     *
     * @param username the user that sent the message
     * @param message  the message that desired to be added
     */
    public void addMessageToPanel(String username, String message, boolean isLocal) {
        messagePanel.setText(messagePanel.getText().toString()
            + "\n"
            + username
            + ": "
            + message);
        if(isLocal) {
            messageText.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        launchActivity(ChatListActivity.class);
    }

    /**
     * {LaunchActivity is starting }.
     */
    public void launchActivity(Class activityToLaunch) {
        this.finish();
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_left1, R.transition.anim_left2);
    }

    /**
     * To fetch messages and display them on screen.
     */
    public void getMessagesToDisplay() {
        messagePanel.setText("");
        for (MessageModel message : currentConversation.getMessages()) {
            addMessageToPanel(message.getFrom().getUsername(), message.getMessage(), false);
        }
    }

    /**
     * Listener for new messages.
     */
    @Override
    public void onNewMessageReceived() {
        runOnUiThread(this);
    }

    /**
     * Invokes the display refresher function.
     * Needs to be in this UIThread function because android does not
     *      allow changes on UI from other threads.
     */
    @Override
    public void run() {
        getMessagesToDisplay();
    }
}
