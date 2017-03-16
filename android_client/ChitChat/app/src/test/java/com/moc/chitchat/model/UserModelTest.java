package com.moc.chitchat.model;

import android.util.Base64;

import com.moc.chitchat.crypto.CryptoBox;

import java.security.KeyPair;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UserModelTest {

    @Test
    public void testConstructor() {
        String expectedUsername = "Jacob";
        UserModel userModel = new UserModel(expectedUsername);

        assertEquals(userModel.getUsername(), expectedUsername);
    }

    @Test
    public void testSetPassword() {
        String userName = "John";
        UserModel userModel = new UserModel(userName);
        String expectedPassword = "Cyber_Security";
        userModel.setPassword(expectedPassword);

        assertEquals(expectedPassword, userModel.getPassword());
    }

    @Test
    public void testSetPasswordCheck() {
        String userName = "George";
        UserModel userModel = new UserModel(userName);
        String expectedPassword = "Cyber_Security";
        userModel.setPasswordCheck(expectedPassword);

        assertEquals(expectedPassword, userModel.getPasswordCheck());
    }

    @Test
    public void testSetAuthToken() {
        String userName = "George";
        UserModel userModel = new UserModel(userName);
        String expectedAuthToken = "jahgewlgwGfewFwe34gbrg342k2pkpf9";
        userModel.setAuthToken(expectedAuthToken);

        assertEquals(expectedAuthToken, userModel.getAuthToken());
    }

    @Test
    public void testToJSONObjectForRegister() {
        String expectedUserName = "Ozhan";
        UserModel userModel = new UserModel(expectedUserName);
        String expectedPassword = "Security123";
        userModel.setPassword(expectedPassword);

        JSONObject userJson = userModel.toJsonObjectForRegister();
        try {
            assertEquals(expectedUserName, userJson.getString("username"));
            assertEquals(expectedPassword, userJson.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
