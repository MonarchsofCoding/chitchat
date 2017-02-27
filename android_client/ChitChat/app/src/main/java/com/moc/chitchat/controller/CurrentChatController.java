package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.model.MessageModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import javax.inject.Inject;



/* Provides the actions performed by the CurrentChatActivity
 */

public class CurrentChatController {

    /* HttpClient
     */
    private HttpClient httpClient;

    /* SessionConfiguration
     */
    private SessionConfiguration sessionConfiguration;

    /* CurrentChatConfiguration
     */
    private CurrentChatConfiguration currentChatConfiguration;

    /**
     * {@CurrentChatController}.
     */
    @Inject
    public CurrentChatController(
        HttpClient httpClient,
        SessionConfiguration sessionConfiguration,
        CurrentChatConfiguration currentChatConfiguration

    ) {
        this.httpClient = httpClient;
        this.sessionConfiguration = sessionConfiguration;
        this.currentChatConfiguration = currentChatConfiguration;
    }

    /**
     * {@sendMessageToRecipientr}.
     */
    public void sendMessageToRecipient(
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        MessageModel message,
        Map<String, String> requestHeaders
    ) throws JSONException {
        // Make a POST request to send the message.
        this.httpClient.sendRequestWithHeader(
            context,
            Request.Method.POST,
            "/api/v1/messages",
            message.toJSONObject(),
            successListener,
            errorListener,
            requestHeaders
        );
    }
}
