package com.moc.chitchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.service.ReceiveMessageService;

import java.util.ArrayList;

import javax.inject.Inject;

public class ChatListActivity extends AppCompatActivity
    implements TabLayout.OnTabSelectedListener,
    DialogInterface.OnClickListener,
    ListView.OnItemClickListener {

    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;
    @Inject
    SessionConfiguration sessionConfiguration;

    ListView chatsList;
    TabLayout menuTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        sessionConfiguration.setCurrentActivity(this);

        this.setContentView(R.layout.activity_chat_list);
        getSupportActionBar().setTitle("ChitChat");

        chatsList = (ListView) findViewById(R.id.chats_list);
        chatsList.setOnItemClickListener(this);

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(0);
        tab.select();

        menuTabs.addOnTabSelectedListener(this);

        getConversations();
    }

    /**
     * Gets every ongoing conversation.
     */
    public void getConversations() {
        ArrayList<UserModel> chatsArrayList = new ArrayList<UserModel>();

        ArrayList<ConversationModel> conversations = chitChatMessagesConfiguration
            .getConversations();
        for (ConversationModel conv : conversations) {
            chatsArrayList.add(conv.getOtherParticipant());
        }

        ArrayAdapter<UserModel> arrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            chatsArrayList);

        chatsList.setAdapter(arrayAdapter);
    }

    //When the user clicks on a chat.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserModel newRecipient = (UserModel) parent.getItemAtPosition(position);

        currentChatConfiguration.setCurrentRecipient(newRecipient);

        this.finish();

        Intent currentChatIntent = new Intent(getBaseContext(), CurrentChatActivity.class);
        startActivity(currentChatIntent);
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();

        TabLayout.Tab tabToSelect = menuTabs.getTabAt(0); //Locks the Search tab since we may
        tabToSelect.select();                             //stay in this activity

        if (tabName.equals("Search Users")) {
            this.finish();
            launchActivityFromTab(SearchUserActivity.class);

        } else if (tabName.equals("Current Chat")) {
            if (currentChatConfiguration.getCurrentRecipient() != null) {
                String currentReceiverUsername = currentChatConfiguration
                    .getCurrentRecipient().getUsername();
                if (!currentReceiverUsername.equals("")) {
                    this.finish();

                    Intent currentChatIntent = new Intent(getBaseContext(), CurrentChatActivity.class);
                    currentChatIntent.putExtra(
                        "recipient_username",
                        currentReceiverUsername);
                    startActivity(currentChatIntent);

                    overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
                }
            } else {
                Toast.makeText(this, String.format("There is no current chat"), Toast.LENGTH_LONG).show();
            }
        }
    }

    //For a unselected tab
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //Basically do nothing, at the moment at least.
    }

    //For a re-selected tab
    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        System.out.println("Tab: " + tab.getText().toString() + " is reselected.");
        //Basically do nothing.
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exitAlertBuilder = new AlertDialog.Builder(this);
        AlertDialog exitAlert = exitAlertBuilder.create();
        exitAlert.setCancelable(false);
        exitAlert.setTitle("Logging out");
        exitAlert.setMessage("Do you wish to logout?");
        exitAlert.setButton(DialogInterface.BUTTON_POSITIVE,"Yes",this);
        exitAlert.setButton(DialogInterface.BUTTON_NEGATIVE,"No",this);
        exitAlert.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            sessionConfiguration.cleanCurrentUser();
            currentChatConfiguration.cleanCurrentRecipient();
            chitChatMessagesConfiguration.clearChitChatMessagesConfiguration();
            stopService(new Intent(getBaseContext(), ReceiveMessageService.class));
            this.finish();
        }
    }

    /**
     * {LaunchActivity is starting }.
     */
    public void launchActivityFromTab(Class activityToLaunch) {
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
    }
}
