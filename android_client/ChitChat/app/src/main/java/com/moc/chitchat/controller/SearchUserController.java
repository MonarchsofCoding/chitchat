package com.moc.chitchat.controller;

/**
 * Created by aakyo on 16/02/2017.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.client.HttpClient;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Provides the actions performed by the SearchUserActivity
 */
public class SearchUserController {

    /**
     * HttpClient
     */
    private HttpClient httpClient;

    @Inject
    public SearchUserController(HttpClient httpClient){
        this.httpClient = httpClient;
    }

    public void searchUser (
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String queryString)
    {
        // Make a GET request to find all the connected users.
        this.httpClient.sendRequest(
            context,
            Request.Method.GET,
            "/api/v1/users?username=" + queryString,
            null,
            successListener,
            errorListener
        );
    }


}
