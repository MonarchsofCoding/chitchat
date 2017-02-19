package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by spiros on 16/02/17.
 */
public class UserSearchControllerTest {

    @Mock private HttpClient mockHttpClient;
    @Mock private UserResolver mockuserresolver;

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

        Map<String, Object> mockmapper = new HashMap<String, Object>();
        mockmapper.put("username", "john");

        UserModel user = new UserModel("john");

        JsonNode thebody = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(thebody);

        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.get("/api/v1/users", mockmapper))
                .thenReturn(mockResponse);

        // Create and define the mocked response to return 200 (success)
        when(mockResponse.getStatus())
                .thenReturn(200);

        String name = "john";
        JSONObject username = new JSONObject();
        username.put("username", name);

        JSONArray data = new JSONArray();
        data.put(username);

        JSONObject obj = new JSONObject();
        obj.put("data", data);

        when(thebody.getObject()).thenReturn(obj);

        List<UserModel> foundUsers = new ArrayList<>();

        // Run the function to test
        foundUsers = this.userSearchController.searchUser("john");

        assertEquals(1, foundUsers.size());
        assertEquals(user.getUsername(), foundUsers.get(0).getUsername());

    }
}
