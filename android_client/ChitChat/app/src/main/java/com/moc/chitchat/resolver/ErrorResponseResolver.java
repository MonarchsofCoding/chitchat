package com.moc.chitchat.resolver;

import com.android.volley.VolleyError;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * ErrorResponseResolver provides utility methods for use in erroneous responses.
 */
public class ErrorResponseResolver {

    /**
     * Returns the JSONObject from the error response.
     * @param error the error from Volley
     * @return The body as a JSONObject
     */
    public JSONObject getResponseBody(VolleyError error) {
        try {
            //When no server connection, exception happens.
            return new JSONObject(new String(error.networkResponse.data, "UTF-8"));
        } catch (JSONException | UnsupportedEncodingException exce) {
            exce.printStackTrace();
        }

        return new JSONObject();
    }
}
