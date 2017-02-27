package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONObject;



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
     * */
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
     * {@sendMessageToRecipientr}. */
    public void sendMessageToRecipient(
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String message,
        String recipient,
        Map<String,String> requestHeaders
    ) {
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("recipient", recipient);

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
