package com.moc.chitchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * CurrentChatActivity provides the View and Actions involved with searching a User.
 */

public class CurrentChatActivity extends AppCompatActivity
    implements View.OnClickListener,
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

    @Inject
    CurrentChatController currentChatController;
    @Inject
    SessionConfiguration sessionConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;
    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        currentReceiverUsername = getIntent().getStringExtra("recipient_username");

        currentReceiver = new UserModel(currentReceiverUsername);

        currentConversation = chitChatMessagesConfiguration
            .getConversation(new UserModel(currentReceiverUsername));

        this.setContentView(R.layout.activity_current_chat);
        getSupportActionBar().setTitle(currentReceiverUsername);

        messageText = (TextView) findViewById(R.id.message_text);

        messagePanel = (TextView) findViewById(R.id.message_panel);
        for (MessageModel message : currentConversation.getMessages()) {
            addMessageToPanel(message.getFrom().getUsername(), message.getMessage());
        }

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(2);
        tab.select();
        menuTabs.addOnTabSelectedListener(this);
    }

    //For when a button is clicked
    @Override
    public void onClick(View view) {
        if (!messageText.getText().toString().equals("")) {
            Map<String, String> requestHeaders = new HashMap<String, String>();
            requestHeaders.put(
                "authorization",
                "Bearer " + sessionConfiguration.getCurrentUser().getAuthToken());

            try {
                currentChatController.sendMessageToRecipient(
                    this,
                    this,
                    this,
                    new MessageModel(
                        currentReceiver,
                        messageText.getText().toString()
                    ),
                    requestHeaders
                );
            } catch (JSONException jsonexception) {
                jsonexception.printStackTrace();
                Toast.makeText(this,
                    "Error caused by JSONObject: " + jsonexception.getMessage(),
                    Toast.LENGTH_LONG);
            }
        }
    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        try {
            System.out.println(new JSONObject(new String(error.networkResponse.data)));
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            String from = response.getJSONObject("data").get("sender").toString();
            String to = response.getJSONObject("data").get("recipient").toString();
            String message = response.getJSONObject("data").get("message").toString();

            UserModel fromUser = new UserModel(from);
            UserModel toUser = new UserModel(to);

            chitChatMessagesConfiguration.addMessageToConversation(
                toUser,
                new MessageModel(fromUser, toUser, message)
            );

            addMessageToPanel(from, message);
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
            launchActivityFromTab(ChatListActivity.class);
        } else if (tabName.equals("Search Users")) {
            launchActivityFromTab(SearchUserActivity.class);
        }
        this.finish();
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
    public void addMessageToPanel(String username, String message) {
        messagePanel.setText(messagePanel.getText().toString()
            + "\n"
            + username
            + ": "
            + message);
        messageText.setText("");
    }

    @Override
    public void onBackPressed() {
        //TODO: Dialog to go back to chats
        System.out.println("The back button is pressed.");
    }

    /**
     * {LaunchActivity is starting }.
     */
    public void launchActivityFromTab(Class activityToLaunch) {
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_left1, R.transition.anim_left2);
    }
}
