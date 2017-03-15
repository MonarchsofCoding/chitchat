package com.moc.chitchat.channel;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.controller.MessageController;
import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * UserMessageChannelTest provides tests for the UserMessageChannel
 */
public class UserMessageChannelTest {

    @Mock
    private Configuration configuration;

    @Mock
    private MessageController messageController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        UserMessageChannel userMessageChannel = new UserMessageChannel(configuration, messageController);

        assertNotNull(userMessageChannel);
        assertEquals(UserMessageChannel.class, userMessageChannel.getClass());
    }

    @Test
    public void testGetJoin() {
        UserModel userModel = new UserModel("JohnCena");
        String authToken = "asdflrewqasdfrewqqwerasdf";
        userModel.setAuthToken(authToken);

        when(configuration.getLoggedInUser()).thenReturn(userModel);
        UserMessageChannel userMessageChannel = new UserMessageChannel(configuration, messageController);
        JSONObject jsonObject = userMessageChannel.getJoin();

        assertEquals("user:"+ userModel.getUsername(), jsonObject.get("topic"));
        assertEquals(authToken, jsonObject.getJSONObject("payload").get("authToken"));

    }

    @Test
    public void testHandleMessage() {

        UserMessageChannel userMessageChannel = new UserMessageChannel(configuration, messageController);
        JSONObject payload = mock(JSONObject.class);
        when(payload.getString("body")).thenReturn("some text");
        when(payload.getString("from")).thenReturn("name");

        userMessageChannel.handleMessage(payload);
    }

    @Test
    public void testGetEvent() {
        UserMessageChannel userMessageChannel = new UserMessageChannel(configuration, messageController);

        assertEquals("new:message", userMessageChannel.getEvent());
    }
}
