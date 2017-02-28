package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class SessionConfigurationTest {

    @Test
    public void testSetCurrentUser() {
        String userName = "John";
        UserModel userModel = new UserModel(userName);
        String expectedUsername = "John";

        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        sessionConfiguration.setCurrentUser(userModel);

        assertEquals(expectedUsername, sessionConfiguration.getCurrentUser().getUsername());
    }

    @Test
    public void testCleanCurrentUser() {
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        sessionConfiguration.cleanCurrentUser();
    }

    @Test
    public void testStatusChangeOnActivity() {
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        sessionConfiguration.setCurrentChatActivityStatus(true);

        assertEquals(true,sessionConfiguration.isCurrentChatActivityRunning());
    }
}
