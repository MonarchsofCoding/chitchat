package com.moc.chitchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.controller.SearchUserController;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import com.moc.chitchat.service.ReceiveMessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * SearchUserActivity provides the View and Actions involved with searching a User.
 */

public class SearchUserActivity extends AppCompatActivity
    implements TabLayout.OnTabSelectedListener,
    SearchView.OnQueryTextListener,
    ListView.OnItemClickListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    @Inject
    SearchUserController searchUserController;
    @Inject
    ErrorResponseResolver errorResponseResolver;
    @Inject
    SessionConfiguration sessionConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;

    TabLayout menuTabs;
    SearchView searchBar;
    ListView usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        this.setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(1);
        tab.select();
        menuTabs.addOnTabSelectedListener(this);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(this);

        usersList = (ListView) findViewById(R.id.users_list);
        usersList.setOnItemClickListener(this);
    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error searching a user");
        try {
            Toast.makeText(this,
                String.format("The user you are trying to found is not connected or not existing"),
                Toast.LENGTH_LONG).show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            List<String> userList = new ArrayList<String>();

            JSONArray usernameArray = (JSONArray) response.get("data");
            for (int i = 0; i < usernameArray.length(); i++) {
                userList.add(usernameArray.getJSONObject(i).get("username").toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                userList);

            usersList.setAdapter(arrayAdapter);
        } catch (Exception exept) {
            exept.printStackTrace();
        }
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();

        TabLayout.Tab tabToSelect = menuTabs.getTabAt(1); //Locks the Search tab since we may
        tabToSelect.select();                             //stay in this activity

        if (tabName.equals("Chats")) {
            launchActivityFromTab(ChatListActivity.class);
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

    //When a user submits a search
    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() >= 3) {
            Map<String, String> requestHeaders = new HashMap<String, String>();
            requestHeaders.put(
                "authorization",
                "Bearer " + sessionConfiguration.getCurrentUser().getAuthToken());
            searchUserController.searchUser(
                this,
                this,
                this,
                query,
                requestHeaders
            );
        } else {
            Toast.makeText(this,
                String.format("You can only do a search with an input longer than 3 characters"),
                Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //When the search text is changed.
    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.contains(" ")) {
            searchBar.setQuery(newText.replace(" ", ""), false);
        }
        return false;
    }

    //When the user clicks on an user.
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO: Dialog to ask exiting
        stopService(new Intent(getBaseContext(), ReceiveMessageService.class));
        this.finish();
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
