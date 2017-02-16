package com.moc.chitchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.SearchView;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.controller.SearchUserController;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by aakyo on 16/02/2017.
 */

public class SearchUserActivity extends Activity
    implements View.OnClickListener,
    TabLayout.OnTabSelectedListener,
    SearchView.OnQueryTextListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    @Inject SearchUserController searchUserController;
    @Inject ErrorResponseResolver errorResponseResolver;


    TabLayout menuTabs;
    SearchView searchBar;

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
    }

    //For buttons
    @Override
    public void onClick(View v) {

    }

    //For Volley Error response
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println(error);
        //TODO: Implement error handling
    }

    //For Volley Success response
    @Override
    public void onResponse(JSONObject response) {
        System.out.println(response);
        //TODO: Implement filling the ListView according to the response
    }

    //For a selected tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String tabName = tab.getText().toString();
        if(tabName.equals("Chats")) {
            System.out.println(1);
            //TODO: Open Chats Activity
        }
        else if(tabName.equals("Current Chat")) {
            System.out.println(2);
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
}
