package com.moc.chitchat.controller;

import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * UserSearchController is the controller for the user search feature.
 */
@Component
public class UserSearchController {

    private HttpClient httpClient;
    private UserResolver userResolver;
    private UserValidator userValidator;

    /**
     * Constructor for the UserSearchController using the parameters below.
     * @param httpClient - the http client
     * @param userResolver - Resolver for the user
     * @param userValidator - Validator for the user
     */
    @Autowired
    public UserSearchController(
            HttpClient httpClient,
            UserResolver userResolver,
            UserValidator userValidator
    ) {
        this.httpClient = httpClient;
        this.userResolver = userResolver;
        this.userValidator = userValidator;
    }

    /**
     * searchUser function looks for the users using the parameter below.
     * @param username - the name of te user
     * @return - a list of the users that matches the string
     * @throws UnexpectedResponseException - unexpected response
     * @throws ValidationException - If not enough characters inserted
     * @throws IOException - If invalid
     * @throws NoSuchAlgorithmException - cryptographic algorithm is requested but is not available in the environment
     * @throws InvalidKeySpecException - invalid key specification
     */
    public List<UserModel> searchUser(String username)
            throws UnexpectedResponseException, ValidationException, IOException,
            InvalidKeySpecException, NoSuchAlgorithmException {
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("username", username);

        Response response = this.httpClient.get("/api/v1/users", mapper);

        if (response.code() == 400) {
            this.userValidator.throwErrorsFromResponse(response);
        } else if (response.code() != 200) {
            throw new UnexpectedResponseException(response);
        }

        String jsonData = response.body().string();
        JSONObject jsonObject = new JSONObject(jsonData);


        JSONArray jsonArray = jsonObject.getJSONArray("data");
        List<UserModel> foundUsers = new ArrayList<>();

        for (Object obj : jsonArray) {
            JSONObject jsonobjectname = (JSONObject) obj;
            foundUsers.add(userResolver.getUserModelViaJSonObject(jsonobjectname));
        }

        return foundUsers;
    }

}
