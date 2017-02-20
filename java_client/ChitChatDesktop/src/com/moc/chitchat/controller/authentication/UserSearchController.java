package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
@Component
public class UserSearchController {

    private HttpClient httpClient;
    private UserResolver userResolver;

    @Autowired
    public UserSearchController(
            HttpClient httpClient,
            UserResolver userResolver
    ){
        this.httpClient = httpClient;
        this.userResolver = userResolver;
    }

    public List<UserModel> searchUser(String username) throws UnirestException, UnexpectedResponseException {
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("username", username);

        HttpResponse<JsonNode> response = this.httpClient.get("/api/v1/users", mapper);

        if (response.getStatus() != 200) {
            // Unexpected response code. e.g. 500
            throw new UnexpectedResponseException(response);
        }

        JSONArray jsonArray = response.getBody().getObject().getJSONArray("data");
        List<UserModel> foundUsers = new ArrayList<>();

        for(Object obj: jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            foundUsers.add(userResolver.getUserModelViaJSonObject(jsonObject));
        }
        return foundUsers;
    }

}
