package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;

import org.json.JSONObject;

import javax.inject.Inject;

/**
 * Created by aakyo on 14/02/2017.
 */

public class LoginController {

    /**
     * UserResolver
     */
    private UserResolver userResolver;

    /**
     * HttpClient
     */
    private HttpClient httpClient;

    /**
     * SessionConfiguration
     */
    private SessionConfiguration sessionConfiguration;

    /**
     * LoginController constructor.
     * @param userResolver To resolve parameters into User objects.
     * @param httpClient To send HTTP(S) requests.
     */
    @Inject
    public LoginController(
        UserResolver userResolver,
        HttpClient httpClient,
        SessionConfiguration sessionConfiguration

    ) {
        this.userResolver = userResolver;
        this.httpClient = httpClient;
        this.sessionConfiguration = sessionConfiguration;
    }

    /**
     * loginUser logges in a User on the backend.
     * No exception thrown, since there is no local validation happens
     * @param context the Android Context.
     * @param successListener The HTTP success listener.
     * @param errorListener The HTTP error listener.
     * @param username The username of the User.
     * @param password The password of the User.
     */
    public void loginUser(
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String username, String password) {

        System.out.println(
            String.format("Username: %s, Password length: %s",
                username, password.length())
        );

        // Create a User object
        UserModel user = this.userResolver.createLoginUser(
            username,
            password
        );


        /**
         * Setting the current user object
         * If the login is successful, this line is doing a correct action.
         * If not, then the cleanCurrentUser in ErrorResponse ensures no false login happened.
         */
        this.sessionConfiguration.setCurrentUser(user);

        // Make a POST request to login with the User object.
        this.httpClient.sendRequest(
            context,
            Request.Method.POST,
            "/api/v1/auth",
            user.toJsonObject(),
            successListener,
            errorListener
        );
    }
}
