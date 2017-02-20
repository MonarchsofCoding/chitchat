package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * UserSearchController is the controller for the user search feature
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

            if(response.getStatus()==500) {

                throw new UnirestException("Server error connection") ;
            }
            else throw new UnexpectedResponseException(response);
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
