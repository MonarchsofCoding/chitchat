package com.moc.chitchat.client;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.moc.chitchat.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient provides an smaller API wrapper around Volley for the methods we use.
 */
public class HttpClient {

    /**
     * RequestQueue
     */
    private RequestQueue requestQueue;

    /**
     * getRequestQueue returns the RequestQueue for the given Context.
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
     * sendRequest sends a JSONObject request to the given URI.
     * @param context the current context.
     * @param method the HTTP method.
     * @param uri the URI.
     * @param body the body as a JSONObject.
     * @param successListener the object containing the function to call on success.
     * @param errorListener the object containing the function to call on error.
     */
    public void sendRequest(
            Context context,
            int method,
            String uri,
            JSONObject body,
            Response.Listener<JSONObject> successListener,
            Response.ErrorListener errorListener
    ) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                method,
                String.format("%s%s",
                    context.getResources().getString(R.string.server_url),
                    uri
                ),
                body,
                successListener,
                errorListener
        );

        this.getRequestQueue(context).add(jsonObjectRequest);
    }

    /**
     * sendRequestWithHeader sends a JSONObject request to the given URI with provided header.
     * @param context the current context.
     * @param method the HTTP method.
     * @param uri the URI.
     * @param body the body as a JSONObject.
     * @param successListener the object containing the function to call on success.
     * @param errorListener the object containing the function to call on error.
     * @param requestHeaders the Map containing the headers for the request.
     */
    public void sendRequestWithHeader(
        final Context context,
        int method,
        String uri,
        JSONObject body,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        final Map<String, String> requestHeaders
    ) {
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
            /**
             * getHeaders Overridden method for fetching the headers
             * @return the headers to the request
             */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headerParams = new HashMap<String, String>();
                if (requestHeaders != null) {
                    for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                        headerParams.put(header.getKey(),header.getValue());
                    }
                }
                return headerParams;
            }
        };
        this.getRequestQueue(context).add(jsonObjectRequest);
    }
}
