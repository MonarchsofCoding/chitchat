package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;

import java.util.Map;

import javax.inject.Inject;

import org.json.JSONObject;


/* Provides the actions performed by the SearchUserActivity.
 */
public class SearchUserController {

    /**
     * HttpClient.
     */
    private HttpClient httpClient;

    /* SessionConfiguration.
     */
    private SessionConfiguration sessionConfiguration;

    @Inject
    public SearchUserController(
        HttpClient httpClient,
        SessionConfiguration sessionConfiguration
    ) {
        this.httpClient = httpClient;
        this.sessionConfiguration = sessionConfiguration;
    }

    /**
     * @param context         the Android Context.
     * @param successListener The HTTP success listener.
     * @param errorListener   The HTTP error listener.
     * @param queryString     the searched username string.
     */
    public void searchUser(
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String queryString
    ) {

        // Make a GET request to find all the connected users.
        this.httpClient.sendRequest(
            context,
            Request.Method.GET,
            String.format("/api/v1/users?username=%s", queryString),
            null,
            successListener,
            errorListener,
            true
        );
    }
}
