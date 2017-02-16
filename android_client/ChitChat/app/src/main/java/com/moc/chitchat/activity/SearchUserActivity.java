package com.moc.chitchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.controller.SearchUserController;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by aakyo on 16/02/2017.
 */

public class SearchUserActivity extends Activity
    implements View.OnClickListener,
    TabLayout.OnTabSelectedListener,
    SearchView.OnQueryTextListener,
    ListView.OnItemClickListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    @Inject SearchUserController searchUserController;
    @Inject ErrorResponseResolver errorResponseResolver;


    TabLayout menuTabs;
    SearchView searchBar;
    ListView usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        this.setContentView(R.layout.activity_search);
        this.getWindow().setTitle("Search");

        menuTabs = (TabLayout) findViewById(R.id.menu_tabs);
        TabLayout.Tab tab = menuTabs.getTabAt(1);
        tab.select();
        menuTabs.addOnTabSelectedListener(this);

        searchBar = (SearchView) findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(this);

        usersList = (ListView) findViewById(R.id.users_list);
    }

    //For buttons
    @Override
    public void onClick(View v) {

    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error registering");
        try {

            Toast.makeText(this,
                String.format("The user you are trying to found is not connected or not existing"),
                Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        try {
            List<String> userList = new ArrayList<String>();

            JSONArray usernameArray = (JSONArray) response.get("data");
            for(int i = 0; i < usernameArray.length(); i++) {
                userList.add(usernameArray.getJSONObject(i).get("username").toString());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                userList);

            usersList.setAdapter(arrayAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();
        if(tabName.equals("Chats")) {
            //TODO: Open Chats Activity
        }
        else if(tabName.equals("Current Chat")) {
            //TODO: Open Current Chat Activity
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
        searchUserController.searchUser(
            this,
            this,
            this,
            query
        );
        return false;
    }

    //When the search text is changed.
    @Override
    public boolean onQueryTextChange(String newText) {
        //Basically do nothing.
        return false;
    }

    //When the user clicks on an user.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Open a chat activity with the user selected.
    }
}
