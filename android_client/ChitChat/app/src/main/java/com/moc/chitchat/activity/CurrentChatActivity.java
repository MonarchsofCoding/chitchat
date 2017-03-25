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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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

        sessionConfiguration.setCurrentActivity(this);

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
            sendButton.setClickable(false);

            keyboardManager.hideSoftInputFromWindow(messageText.getWindowToken(), 0);
            if(currentReceiver.getPublicKey() != null) {
                try {
                    sendMessage();
                } catch (
                    NoSuchAlgorithmException
                    | InvalidKeyException
                    | NoSuchPaddingException
                    | UnsupportedEncodingException
                    | BadPaddingException
                    | IllegalBlockSizeException ex
                ) {
                    ex.printStackTrace();
                    Toast.makeText(this,
                        String.format("Error caused by Encryption System: %s", ex.getMessage()),
                        Toast.LENGTH_LONG);
                }
            } else {
                try {
                    currentChatController.getRecipientPublicKey(
                        this,
                        this,
                        this,
                        currentReceiverUsername
                    );
                } catch (JSONException jsonexception) {
                    jsonexception.printStackTrace();
                    Toast.makeText(this,
                        String.format("Error caused by JSONObject: %s", jsonexception.getMessage()),
                        Toast.LENGTH_LONG);
                }
            }
        }
    }

    /**
     * Invokes message sending functionality.
     */
    public void sendMessage() throws
            NoSuchPaddingException,
            BadPaddingException,
            NoSuchAlgorithmException,
            IllegalBlockSizeException,
            UnsupportedEncodingException,
            InvalidKeyException {
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
                String.format("Error caused by JSONObject: %s", jsonexception.getMessage()),
                Toast.LENGTH_LONG);
        }
    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        sendButton.setClickable(true);
        try {
            JSONObject response = this.errorResponseResolver.getResponseBody(error);

            if(response.has("errors")) {
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
            }
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            if(!((JSONObject) response.get("data")).has("public_key")) {
                sendButton.setClickable(true);

                String from = response.getJSONObject("data").get("sender").toString();

                UserModel fromUser = new UserModel(from);
                UserModel toUser = currentMessage.getTo();

                chitChatMessagesConfiguration.addMessageToConversation(
                    toUser,
                    new MessageModel(fromUser, toUser, currentMessage.getMessage()),
                    true
                );
                addMessageToPanel(from, currentMessage.getMessage(), true);
                System.out.println(String.format("Message from %s is sent to %s",
                    fromUser.getUsername(), toUser.getUsername()));
                System.out.println(String.format("The sent message: %s", currentMessage.getMessage()));
            } else {
                String publicKey = ((JSONObject) response.get("data")).get("public_key").toString();
                currentChatConfiguration.setCurrentRecipient(
                    currentChatConfiguration.getCurrentRecipient().setPublicKey(
                        cryptoBox.pubKeyStringToKey(publicKey)
                    )
                );
                sendMessage();
            }
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
            Toast.makeText(this,
                String.format("Error caused by JSONObject: %s", jsonexception.getMessage()), Toast.LENGTH_LONG);
        } catch (
            NoSuchAlgorithmException
            | InvalidKeyException
            | NoSuchPaddingException
            | BadPaddingException
            | UnsupportedEncodingException
            | InvalidKeySpecException
            | IllegalBlockSizeException exception
        ) {
            exception.printStackTrace();
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
        System.out.println(String.format("Tab: %s is reselected.", tab.getText().toString()));
    }

    /**
     * To add a message to the messagePanel with the username.
     *
     * @param username the user that sent the message
     * @param message  the message that desired to be added
     */
    public void addMessageToPanel(String username, String message, boolean isLocal) {
        messagePanel.setText(String.format("%s \n %s : %s",
            messagePanel.getText().toString(),
            username,
            message));
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
