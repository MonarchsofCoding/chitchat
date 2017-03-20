package com.moc.chitchat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;

import org.json.JSONObject;

import java.security.KeyPair;

/**
 * Provides helpers for sending messages during tests.
 */
public class MessageHelper {


    public void sendMessage(String from, String fromPassword, String to, String message) throws Exception {

        UserResolver userResolver = new UserResolver();
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        HttpClient client = new HttpClient(sessionConfiguration);
        CryptoBox cryptoBox = new CryptoBox();
        cryptoBox.initialize();

        // create fromUser
        KeyPair fromKeyPair = cryptoBox.generateKeyPair();
        UserModel fromUser = userResolver.createLoginUser(
            from,
            fromPassword,
            fromKeyPair.getPublic(),
            fromKeyPair.getPrivate()
        );

        // Authenticate fromUser
        client.sendRequest(
                null,
                Request.Method.POST,
                "/api/v1/auth",
                fromUser.toJsonObjectForLogin(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                false
        );


        // get Public Key for toUser
        client.sendRequest(
                null,
                Request.Method.GET,
                String.format("/api/v1/users/%s", to),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                },
                false
        );
    }

}
