package com.moc.chitchat;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserResponseValidator {

    public String validateResponse (VolleyError error) {
        StringBuilder response = new StringBuilder();

        try {
            JSONObject serverErrors = new JSONObject(new String(error.networkResponse.data)).getJSONObject("errors");
            if (serverErrors.has("username")) {
                JSONArray usernameErrors = serverErrors.getJSONArray("username");
                response.append("Username ");
                for (int i = 0; i < usernameErrors.length(); i++) {
                    response.append(usernameErrors.get(i).toString());
                }
            }

            if (serverErrors.has("password")) {
                JSONArray passwordErrors = serverErrors.getJSONArray("password");
                response.append("Password ");
                for (int i = 0; i < passwordErrors.length(); i++) {
                    response.append(passwordErrors.get(i).toString());
                }
            }
        } catch (Exception e){
            response.append("Unexpected error happened. ");
            if (error.networkResponse != null) {
                response.append("Response code: " + error.networkResponse.statusCode);
            }
            else {
                response.append("Server is unreachable.");
            }
        }
        finally {
            return response.toString();
        }
    }
}
