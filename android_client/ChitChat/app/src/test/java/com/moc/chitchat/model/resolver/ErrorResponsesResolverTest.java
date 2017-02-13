package com.moc.chitchat.model.resolver;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import java.io.UnsupportedEncodingException;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

/**
 * ErrorResponsesResolverTest provides the tests for the ErrorResponsesResolver
 */

public class ErrorResponsesResolverTest {

    @Test
    public void testNoResponseBody() throws JSONException, UnsupportedEncodingException{
        byte [] bytes = new byte[0];
        NetworkResponse networkResponse = new NetworkResponse(500, bytes, null, true, 500);

        VolleyError volleyError = new VolleyError(networkResponse);

        ErrorResponseResolver errorResponse = new ErrorResponseResolver();
        JSONObject jsonObject = errorResponse.getResponseBody(volleyError);

        assertEquals(jsonObject.getClass(), JSONObject.class);
    }

    @Test
    public void testGetResponseBody() throws JSONException, UnsupportedEncodingException {
        String responseJson = "{}";
        NetworkResponse networkResponse = new NetworkResponse(500, responseJson.getBytes(), null, true, 500);

        VolleyError volleyError = new VolleyError(networkResponse);

        ErrorResponseResolver errorResponse = new ErrorResponseResolver();
        JSONObject jsonObject = errorResponse.getResponseBody(volleyError);

        assertEquals(jsonObject.getClass(), JSONObject.class);
    }



}
