package com.moc.chitchat.channel;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.channel.handler.HandlerInterface;
import com.moc.chitchat.channel.handler.NewMessageHandler;
import com.moc.chitchat.channel.handler.UserLogoutHandler;
import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;


/**
 * UserChannelTest provides tests for the userChannel
 */
public class UserChannelTest {

    @Mock private Set<HandlerInterface> handlers;
    @Mock private Configuration configuration;
    @Mock private NewMessageHandler newMessageHandler;
    @Mock private UserLogoutHandler userLogoutHandler;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);

        assertNotNull(userChannel);
        assertEquals(UserChannel.class, userChannel.getClass());
    }

    @Test
    public void testInvalidHandleMessage() throws Exception {
        String event = "some event";
        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);
        JSONObject payload = new JSONObject();

        userChannel.handleMessage(event, payload);

    }

    @Test
    public void testHandleMessage() throws Exception {
        String text = "{\"topic\":\"user:john\",\"ref\":null,\"payload\":{\"from\":\"bob\",\"body\":\"LbpP9uCKYkCrcKFAwxeabDE+Ij/I/pm+RwomV9kGaevNMAKeARIdZ45JcLeZe+UCSPAPJWn4cdqc9jLxhSlIvC2GeNFb6JHnR7xKSYIW/UMSKDdmX0/dWmLPwYW8boO8rIbUxZ59vGF09S3T9pKeuFOOvIyfXS+igaf8qX7ApZxuoEPYGix/9S2Ra6uuUz+K1uyajl7ory61j43z/O1Rg4eXdK2q+4vLYqQnTaIqe91cOcPYVS1DRBJh+c9HFsm1hbjPuDzE+UC1tPVEI8uNghPcCQn2X/QHUcX8/taCdSxf8ZZ1jkqNCdJH/BNiu5hOcOpwu/qlNm+ByvGL8eCpaTmEBGTMIDeTFXM20MiaKONwtDxX1UGMgIuOv0p77BiIID+cSmcbxUsqNu5n/g0SDsaUUr6fzaFGEB9pSOMrDSFhEKl49/AWLC5U3HPJbfyFmBI+tNQQQ+DimL/TMrTzfQ57bAgiSW5PJ45C0yCioPdisu1akuPIwsEpJdA76EWSwtthRGpwzv6ky8AcioMZ55RnaYOK/lEGkYOyUAiY600khd4H97scgdidexXUNlrobpAaJDCp3oySWKuCEK6OU7CWzxZbJZ+ieyj0VdbWXKQK1Z8j8Q+cVf0CNVg3J0wqE+lFJ7DyIw0Khd1WRYrHjZ8CUMzKs78UEOQ/alTRgBY=\"},\"event\":\"new:message\"}";

        when(newMessageHandler.getEvent()).thenReturn("new:message");

        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);
        JSONObject jsonMsg = new JSONObject(text);

        UserModel user = mock(UserModel.class);
        String authToken = "authToken";
        user.setAuthToken(authToken);
        when(this.configuration.getLoggedInUser()).thenReturn(user);

        userChannel.handleMessage(jsonMsg.get("event").toString(), jsonMsg.getJSONObject("payload"));
    }

    @Test
    public void testGetTopicLoggedIn() {
        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);
        UserModel user = new UserModel("Itachi");
        when(this.configuration.getLoggedInUser()).thenReturn(user);

        String foundName = userChannel.getTopic();

        assertEquals("user:Itachi", foundName);
    }

    @Test
    public void testGetTopicNotLoggedIn() {
        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);

        String foundName = userChannel.getTopic();

        assertEquals("user:not_logged_in", foundName);
    }

    @Test
    public void testGetJoin() {
        UserChannel userChannel = new UserChannel(configuration, newMessageHandler, userLogoutHandler);
        UserModel user = new UserModel("Sasuke");
        String authToken = "authToken";
        user.setAuthToken(authToken);
        when(this.configuration.getLoggedInUser()).thenReturn(user);

        JSONObject jsonObject = userChannel.getJoin();

        assertEquals("phx_join", jsonObject.get("event"));
        assertEquals(String.format("user:%s", user.getUsername()), jsonObject.get("topic"));
        assertEquals(authToken, jsonObject.getJSONObject("payload").get("authToken"));
    }

}
