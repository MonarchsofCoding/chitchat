package com.moc.chitchat.helper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * MessageHelper provides helper functions for sending a message during tests.
 */
public class MessageHelper {

    public static void sendMessage(
            String fromUsername,
            String fromPassword,
            final String toUsername,
            final String message
    ) throws Exception {

        UserResolver userResolver = new UserResolver();
        final SessionConfiguration sessionConfiguration = new SessionConfiguration();
        final HttpClient httpClient = new HttpClient(sessionConfiguration);
        final CryptoBox cryptoBox = new CryptoBox();

        final Context c = InstrumentationRegistry.getTargetContext();

        // Create fromUser (user to send message from)
        KeyPair fromKeyPair = cryptoBox.generateKeyPair();
        final UserModel fromUser = userResolver.createLoginUser(
            fromUsername,
            fromPassword,
            fromKeyPair.getPublic(),
            fromKeyPair.getPrivate()
        );

        // Authenticate fromUser to get authToken
        httpClient.sendRequest(
            c,
            Request.Method.POST,
            "/api/v1/auth",
            fromUser.toJsonObjectForLogin(),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String authToken = response
                            .getJSONObject("data")
                            .getString("authToken")
                        ;
                        fromUser.setAuthToken(authToken);
                        sessionConfiguration.setCurrentUser(fromUser);

                        MessageHelper.afterAuthenticate(
                            c,
                            httpClient,
                            toUsername,
                            cryptoBox,
                            fromUser,
                            message
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private static void afterAuthenticate(
            final Context c,
            final HttpClient client,
            final String toUsername,
            final CryptoBox cryptoBox,
            final UserModel fromUser,
            final String messageStr
    ) {
        // Get Public Key for user you're sending to
        client.sendRequest(
            c,
            Request.Method.GET,
            String.format("/api/v1/users/%s", toUsername),
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String toPubKeyStr = response
                            .getJSONObject("data")
                            .getString("public_key")
                        ;

                        UserModel toUser = new UserModel(toUsername);
                        PublicKey toPubKey = cryptoBox.pubKeyStringToKey(toPubKeyStr);
                        toUser.setPublicKey(toPubKey);

                        MessageHelper.afterGetPubKey(
                            c,
                            client,
                            cryptoBox,
                            fromUser,
                            toUser,
                            messageStr
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public static void afterGetPubKey(
        Context c,
        HttpClient client,
        CryptoBox cryptoBox,
        UserModel fromUser,
        UserModel toUser,
        String messageStr
    ) throws Exception {
        String encMessage = cryptoBox.encrypt(messageStr, toUser.getPublicKey());

        MessageModel message = new MessageModel(fromUser, toUser, encMessage);

        client.sendRequest(
            c,
            Request.Method.POST,
            "/api/v1/messages",
            message.tojsonObject(),
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
            true
        );
    }

}
