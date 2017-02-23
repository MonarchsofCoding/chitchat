package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Provides the actions performed by the CurrentChatActivity
 */

public class CurrentChatController {

    /**
     * HttpClient
     */
    private HttpClient httpClient;

    /**
     * SessionConfiguration
     */
    private SessionConfiguration sessionConfiguration;

    /**
     * CurrentChatConfiguration
     */
    private CurrentChatConfiguration currentChatConfiguration;

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

    public void sendMessageToRecipient (
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String message,
        Map<String,String> requestHeaders
    ) {

        //TODO: Will do a better programming practice here once we implement the message model
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("recipient", currentChatConfiguration.getCurrentRecipientUsername());

        // Make a POST request to send the message.
        this.httpClient.sendRequestWithHeader(
            context,
            Request.Method.POST,
            "/api/v1/messages",
            new JSONObject(messageMap),
            successListener,
            errorListener,
            requestHeaders
        );
    }
}
