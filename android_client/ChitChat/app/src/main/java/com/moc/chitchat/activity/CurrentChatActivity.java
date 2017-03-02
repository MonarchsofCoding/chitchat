package com.moc.chitchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.controller.CurrentChatController;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;




/**
 * CurrentChatActivity provides the View and Actions involved with searching a User.
 */

public class CurrentChatActivity extends Activity
    implements View.OnClickListener,
    TabLayout.OnTabSelectedListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    TabLayout menuTabs;
    TextView recipientLabel;
    TextView messagePanel;
    TextView messageText;
    Button sendButton;

    String currentReceiver;

    @Inject CurrentChatController currentChatController;
    @Inject SessionConfiguration sessionConfiguration;
    @Inject CurrentChatConfiguration currentChatConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        currentReceiver = getIntent().getStringExtra("recipient_username");

        this.setContentView(R.layout.activity_current_chat);
        this.getWindow().setTitle(currentReceiver);

        recipientLabel = (TextView) findViewById(R.id.recipient_label);
        recipientLabel.setText(currentReceiver);

        messagePanel = (TextView) findViewById(R.id.message_panel);

        messageText = (TextView) findViewById(R.id.message_text);

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
        if(!messageText.getText().toString().equals("")) {
            Map<String, String> requestHeaders = new HashMap<String, String>();
            requestHeaders.put(
                "authorization",
                "Bearer " + sessionConfiguration.getCurrentUser().getAuthToken());

            currentChatController.sendMessageToRecipient(
                this,
                this,
                this,
                messageText.getText().toString(),
                currentChatConfiguration.getCurrentRecipientUsername(),
                requestHeaders
            );
        }
    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error sending a message");
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            messagePanel.setText(messagePanel.getText().toString()
                    + "\n"
                    + response.getJSONObject("data").get("sender")
                    + ": "
                    + response.getJSONObject("data").get("message"));
            messageText.setText("");
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();
        //TODO Save chat history to recipient user object in sessionConf
        if(tabName.equals("Chats")) {
            /** LaunchActivityFromTab(Chats.class).*/
        } else if(tabName.equals("Search Users")) {
            launchActivityFromTab(SearchUserActivity.class);
        }
        //ExitActivity();
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

    /** {LaunchActivity is starting }. */
    public void launchActivityFromTab(Class activityToLaunch) {
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_left1,R.transition.anim_left2);
    }
}
