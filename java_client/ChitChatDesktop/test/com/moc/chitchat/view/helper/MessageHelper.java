package com.moc.chitchat.view.helper;


import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;

/**
 * MessageHelper provides helper methods involved with sending messages.
 */
public class MessageHelper {

    /**
     * Sends a message using HTTP from the given username to the given username with the given message.
     * @param fromUsername The username to send from.
     * @param fromPass The password to authenticate with.
     * @param toUsername The username to send to.
     * @param message The message to send.
     * @throws IOException
     */
    public static void sendMessage(String fromUsername, String fromPass, String toUsername, String message) throws IOException {
        Configuration c = new Configuration();
        if (System.getenv("CHITCHAT_ENV") != null && System.getenv("CHITCHAT_ENV").length() > 0) {
            c.setTestingMode();
        } else {
            c.setDevelopmentMode();
        }
        HttpClient client = new HttpClient(c);

        UserModel user = new UserModel(fromUsername);
        user.setPassword(fromPass);

        Response r = client.post("/api/v1/auth", user);
        JSONObject jsonResp = new JSONObject(r.body().string());
        String authToken = jsonResp.getJSONObject("data").getString("authToken");
        user.setAuthToken(authToken);
        c.setLoggedInUser(user);

        UserModel to = new UserModel(toUsername);
        Message m = new Message(user, to, message);
        client.post("/api/v1/messages", m);
    }

}
