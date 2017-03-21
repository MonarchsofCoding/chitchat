package com.moc.chitchat.application;

import android.support.v7.app.AppCompatActivity;

import com.moc.chitchat.model.UserModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;


public class SessionConfigurationTest {

    @Mock
    AppCompatActivity mockCurrentChatActivity;

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
    public void testSetCurrentActivity() {
        SessionConfiguration sessionConfiguration = new SessionConfiguration();
        AppCompatActivity activity = mock(AppCompatActivity.class);
        sessionConfiguration.setCurrentActivity(activity);

        assertEquals(activity,sessionConfiguration.getCurrentActivity());

    }

}
