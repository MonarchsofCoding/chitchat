package com.moc.chitchat.channel.handler;

import com.moc.chitchat.application.Configuration;
import javafx.embed.swing.JFXPanel;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * UserLogOutHandlerTest provides tests for the userlogouthandler
 */
public class UserLogOutHandlerTest {

    @Mock private Configuration mockConfiguration;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        UserLogoutHandler userLogoutHandler = new UserLogoutHandler(mockConfiguration);

        assertNotNull(userLogoutHandler);
        assertEquals(UserLogoutHandler.class, userLogoutHandler.getClass());
    }

    @Test
    public void testGetEvent() {
        UserLogoutHandler userLogoutHandler = new UserLogoutHandler(mockConfiguration);
        String event = userLogoutHandler.getEvent();

        assertEquals("user:logout", event);
    }

    @Test
    public void testHandleMessage() {
        String text = "{}";
        JSONObject payload = new JSONObject(text);

        // Credit: https://rterp.wordpress.com/2015/04/04/javafx-toolkit-not-initialized-solved/
        new JFXPanel();

        UserLogoutHandler userLogoutHandler = new UserLogoutHandler(mockConfiguration);
        userLogoutHandler.handleMessage(payload);
    }

}
