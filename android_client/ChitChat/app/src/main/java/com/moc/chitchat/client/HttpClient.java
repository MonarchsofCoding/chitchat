package com.moc.chitchat.client;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moc.chitchat.R;
import com.moc.chitchat.application.SessionConfiguration;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONObject;

/**
 * HttpClient provides an smaller API wrapper around Volley for the methods we use.
 */
public class HttpClient {

    /* RequestQueue.
     */
    private RequestQueue requestQueue;

    /**
     * SessionConfiguration.
     */
    private SessionConfiguration sessionConfiguration;

    @Inject
    public HttpClient(
        SessionConfiguration sessionConfiguration
    ) {
        this.sessionConfiguration = sessionConfiguration;
    }

    /**
     * getRequestQueue returns the RequestQueue for the given Context.
     *
     * @param context the current context.
     * @return a new request queue if one does not already exist.
     */
    private RequestQueue getRequestQueue(Context context) {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(context);
        }
        return this.requestQueue;
    }

    /**
     * sendRequest sends a JSONObject request to the given URI with provided header/ null header.
     *
     * @param context         the current context.
     * @param method          the HTTP method.
     * @param uri             the URI.
     * @param body            the body as a JSONObject.
     * @param successListener the object containing the function to call on success.
     * @param errorListener   the object containing the function to call on error.
     * @param requireAuth     the boolean to check if the request requires authToken.
     */
    public void sendRequest(
        final Context context,
        int method,
        String uri,
        JSONObject body,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        boolean requireAuth
    ) {
        Map<String, String> requestHeaders = null;

        if(requireAuth) {
            requestHeaders = new HashMap<String, String>();
            requestHeaders.put(
                "authorization",
                String.format("Bearer %s", sessionConfiguration.getCurrentUser().getAuthToken()));
        }

        final Map<String, String> finalRequestHeaders = requestHeaders;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            method,
            String.format("%s%s",
                context.getResources().getString(R.string.server_url),
                uri
            ),
            body,
            successListener,
            errorListener
        ) {
            /* getHeaders Overridden method for fetching the headers.
             * @return the headers to the request.
             */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerParams = new HashMap<String, String>();
                if (finalRequestHeaders != null) {
                    for (Map.Entry<String, String> header : finalRequestHeaders.entrySet()) {
                        headerParams.put(header.getKey(), header.getValue());
                    }
                }
                return headerParams;
            }
        };
        this.getRequestQueue(context).add(jsonObjectRequest);
    }
}
