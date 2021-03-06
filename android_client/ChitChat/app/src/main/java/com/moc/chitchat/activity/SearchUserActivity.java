package com.moc.chitchat.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.controller.SearchUserController;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import com.moc.chitchat.service.ReceiveMessageService;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * SearchUserActivity provides the View and Actions involved with searching a User.
 */

public class SearchUserActivity extends AppCompatActivity
    implements TabLayout.OnTabSelectedListener,
    View.OnClickListener,
    TextWatcher,
    DialogInterface.OnClickListener,
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
    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    CryptoBox cryptoBox;

    TabLayout menuTabs;
    ListView usersList;

    EditText searchText;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        sessionConfiguration.setCurrentActivity(this);

        this.setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(1);
        tab.select();
        menuTabs.addOnTabSelectedListener(this);

        searchText = (EditText) findViewById(R.id.search_layout_text);
        searchText.addTextChangedListener(this);

        searchButton = (Button) findViewById(R.id.search_layout_button);
        searchButton.setOnClickListener(this);

        usersList = (ListView) findViewById(R.id.users_list);
        usersList.setOnItemClickListener(this);
    }

    //For when the search button clicked.
    @Override
    public void onClick(View view) {
        String query = searchText.getText().toString();
        if (query.length() >= 3) {

            System.out.println(String.format("Query made with query text: %s", query));

            searchUserController.searchUser(
                this,
                this,
                this,
                query
            );
        } else {
            Toast.makeText(this,
                String.format("You can only do a search with an input longer than 3 characters"),
                Toast.LENGTH_LONG).show();
            System.out.println("You can only do a search with an input longer than 3 characters");
        }
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

    //Auto-gen method for TextWatcher.
    @Override
    public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {

    }

    //To check for spaces.
    @Override
    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
        if (sequence.toString().contains(" ")) {
            String toPut = sequence.toString().replace(" ", "");
            searchText.setText(toPut);
            searchText.setSelection(toPut.length());
        }
    }

    //Auto-gen method for TextWatcher.
    @Override
    public void afterTextChanged(Editable sequenceEditable) {

    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error searching a user");
        Toast.makeText(this,
            String.format("The user you are trying to found is not connected or not existing"),
            Toast.LENGTH_LONG).show();
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            List<UserModel> userList = new ArrayList<UserModel>();

            JSONArray usernameArray = (JSONArray) response.get("data");
            for (int i = 0; i < usernameArray.length(); i++) {
                UserModel toAdd = new UserModel(usernameArray.getJSONObject(i).get("username")
                        .toString());
                toAdd.setPublicKey(cryptoBox.pubKeyStringToKey(
                        usernameArray.getJSONObject(i).get("public_key").toString())
                );

                userList.add(toAdd);
            }

            ArrayAdapter<UserModel> arrayAdapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    userList);

            usersList.setAdapter(arrayAdapter);
        } catch (InvalidKeySpecException | JSONException exception) {
            exception.printStackTrace();
        }
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();

        TabLayout.Tab tabToSelect = menuTabs.getTabAt(1); //Locks the Search tab since we may
        tabToSelect.select();                             //stay in this activity

        if (tabName.equals("Chats")) {
            this.finish();
            launchActivityFromTab(ChatListActivity.class);
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
        System.out.println(String.format("Tab: %s is reselected.", tab.getText().toString()));
        //Basically do nothing.
    }

    //When the user clicks on an user.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserModel newRecipient = (UserModel) parent.getItemAtPosition(position);

        currentChatConfiguration.setCurrentRecipient(newRecipient);

        Intent currentChatIntent = new Intent(getBaseContext(), CurrentChatActivity.class);
        startActivity(currentChatIntent);

        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
        this.finish();
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

    /**
     * {LaunchActivity is starting }.
     */
    public void launchActivityFromTab(Class activityToLaunch) {
        this.finish();
        Intent toLaunchIntent = new Intent(getBaseContext(), activityToLaunch);
        startActivity(toLaunchIntent);
        overridePendingTransition(R.transition.anim_left1, R.transition.anim_left2);
    }

}
