package com.moc.chitchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.moc.chitchat.model.ConversationModel;
import com.moc.chitchat.model.UserModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by aakyo on 27/02/2017.
 */

public class ChatListActivity extends AppCompatActivity
    implements TabLayout.OnTabSelectedListener,
    ListView.OnItemClickListener {

    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;

    ListView chatsList;
    TabLayout menuTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

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

    //Gets every ongoing conversation
    public void getConversations() {
        ArrayList<String> chatsArrayList = new ArrayList<String>();

        ArrayList<ConversationModel> conversations = chitChatMessagesConfiguration
            .getConversations();
        for (ConversationModel conv : conversations) {
            chatsArrayList.add(conv.getOtherParticipant().getUsername());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_list_item_1,
            chatsArrayList);

        chatsList.setAdapter(arrayAdapter);
    }

    //When the user clicks on a chat.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String newRecipientUsername = parent.getItemAtPosition(position).toString();

        currentChatConfiguration.setCurrentRecipient(new UserModel(newRecipientUsername));

        Intent currentChatIntent = new Intent(getBaseContext(), CurrentChatActivity.class);
        currentChatIntent.putExtra(
            "recipient_username",
            newRecipientUsername);

        startActivity(currentChatIntent);
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
        this.finish();
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();

        TabLayout.Tab tabToSelect = menuTabs.getTabAt(0); //Locks the Search tab since we may
        tabToSelect.select();                             //stay in this activity

        if (tabName.equals("Search Users")) {
            launchActivityFromTab(SearchUserActivity.class);
            this.finish();

        } else if (tabName.equals("Current Chat")) {
            if (currentChatConfiguration.getCurrentRecipient() != null) {
                String currentReceiverUsername = currentChatConfiguration
                    .getCurrentRecipient().getUsername();
                if (!currentReceiverUsername.equals("")) {

                    Intent currentChatIntent = new Intent(getBaseContext(), CurrentChatActivity.class);
                    currentChatIntent.putExtra(
                        "recipient_username",
                        currentReceiverUsername);
                    startActivity(currentChatIntent);

                    overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
                    this.finish();
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

    /**
     * {LaunchActivity is starting }.
     */
    public void launchActivityFromTab(Class activityToLaunch) {
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
    }
}
