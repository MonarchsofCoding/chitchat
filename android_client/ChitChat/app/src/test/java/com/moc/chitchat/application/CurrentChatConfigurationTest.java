package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aakyo on 24/02/2017.
 */

public class CurrentChatConfigurationTest {

    @Test
    public void testSetCurrentRecipientUsername() {
        String userName = "John";
        UserModel userModel = new UserModel(userName);
        String expectedUsername = "John";

        CurrentChatConfiguration currentChatConfiguration = new CurrentChatConfiguration();
        currentChatConfiguration.setCurrentRecipient(userModel);

        assertEquals(expectedUsername, currentChatConfiguration.getCurrentRecipient().getUsername());
    }

    @Test
    public void testCleanCurrentRecipientUsername() {
        CurrentChatConfiguration currentChatConfiguration = new CurrentChatConfiguration();
        currentChatConfiguration.cleanCurrentRecipient();
    }
}
