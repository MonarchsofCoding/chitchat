package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import javax.inject.Inject;

import org.json.JSONObject;


/* Provides the actions performed by the RegistrationActivity
 */
public class RegistrationController {

    /* UserResolver
     */
    private UserResolver userResolver;

    /* UserValidator
     */
    private UserValidator userValidator;

    /* HttpClient
     */
    private HttpClient httpClient;

    /**
     * RegistrationController constructor.
     *
     * @param userResolver  To resolve parameters into User objects.
     * @param userValidator To validate User objects.
     * @param httpClient    To send HTTP(S) requests.
     */
    @Inject
    public RegistrationController(
        UserResolver userResolver,
        UserValidator userValidator,
        HttpClient httpClient

    ) {
        this.userResolver = userResolver;
        this.userValidator = userValidator;
        this.httpClient = httpClient;
    }

    /**
     * registerUser registers a User on the backend. Throws an exception when validation fails.
     *
     * @param context         the Android Context.
     * @param successListener The HTTP success listener.
     * @param errorListener   The HTTP error listener.
     * @param username        The username of the new User.
     * @param password        The password of the new User.
     * @param passwordCheck   The repeated password of the new User.
     * @throws ValidationException the process of validation is not completed
     */
    public void registerUser(
        Context context,
        Response.Listener<JSONObject> successListener,
        Response.ErrorListener errorListener,
        String username, String password, String passwordCheck) throws ValidationException {

        System.out.println(
            String.format("Username: %s, Password length: %s, passwordCheck length: %s",
                username, password.length(), passwordCheck.length())
        );

        // Create a User object
        UserModel user = this.userResolver.createRegisterUser(
            username,
            password,
            passwordCheck
        );

        // Validate the User object locally
        this.userValidator.validate(user);

        // Make a POST request to create a new User object.
        this.httpClient.sendRequest(
            context,
            Request.Method.POST,
            "/api/v1/users",
            user.toJsonObject(),
            successListener,
            errorListener,
            false
        );

    }
}
