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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UserSearchControllerTest provides the tests for UserSearchController
 */
public class UserSearchControllerTest {

    @Mock private HttpClient mockHttpClient;
    @Mock private UserResolver mockUserResolver;

    @Mock
    private HttpResponse<JsonNode> mockResponse;

    @InjectMocks
    private UserSearchController userSearchController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.userSearchController);
        assertEquals(
                this.userSearchController.getClass(),
                UserSearchController.class
        );
    }

    @Test
    public void testSuccessfulSearchUser() throws UnirestException, UnexpectedResponseException {

        Map<String, Object> mockmapper = new HashMap<>();
        mockmapper.put("username", "john");


        UserModel user;
        user = new UserModel("john");

        JsonNode thebody = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(thebody);
      JsonNode bodyResponse = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(bodyResponse);

        bodyResponse = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(bodyResponse);


        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.get("/api/v1/users", mockmapper))
                .thenReturn(this.mockResponse);

        // Create and define the mocked response to return 200 (success)
        when(mockResponse.getStatus())
                .thenReturn(200);

        JSONObject johnJson = new JSONObject();
        johnJson.put("username", "john");

        JSONArray usersJson = new JSONArray();
        usersJson.put(johnJson);


        JSONObject bodyJson = new JSONObject();
        bodyJson.put("data", usersJson);

        when(bodyResponse.getObject()).thenReturn(bodyJson);

        // Mock the UserResolver
        UserModel john = new UserModel("john");
        when(this.mockUserResolver.getUserModelViaJSonObject(johnJson)).thenReturn(john);


        List<UserModel> foundUsers = new ArrayList<>();
        foundUsers.add(mockUserResolver.getUserModelViaJSonObject(johnJson.put("username","john")));
        bodyJson = new JSONObject();
        bodyJson.put("data", usersJson);

        when(bodyResponse.getObject()).thenReturn(bodyJson);

        // Mock the UserResolver
        john = new UserModel("john");
        when(this.mockUserResolver.getUserModelViaJSonObject(johnJson)).thenReturn(john);

        // Run the function to test
        foundUsers = this.userSearchController.searchUser("john");

        assertEquals(1, foundUsers.size());
        assertEquals("john", foundUsers.get(0).getUsername());
    }

    @Test
    public void testServerErrorException() throws UnirestException{
        Map<String, Object> mockmapper = new HashMap<>();
        mockmapper.put("username", "john");

        UserModel user;
        user = new UserModel("john");

        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.get("/api/v1/users", mockmapper))
                .thenReturn(this.mockResponse);

        // Create and define the mocked response to return not 200. i.e failure
        when(mockResponse.getStatus())
                .thenReturn(422);
        try {
            this.userSearchController.searchUser("john");
        } catch (UnexpectedResponseException e) {
            assertEquals("Unexpected Response code: 422", e.getMessage());
        }
    }
}
