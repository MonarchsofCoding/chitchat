package com.moc.chitchat.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *  UserModelTest provides tests for the UserModel
 */
public class UserModelTest {

    @Test
    public void testConstructor()
    {
        String expectedUsername = "Jacob";
        UserModel userModel = new UserModel(expectedUsername);

        assertEquals(userModel.getUsername(), expectedUsername);
    }

    @Test
    public void testSetPassword()
    {
        String userName = "John";
        UserModel userModel = new UserModel(userName);
        String expectedPassword = "Cyber_Security";
        userModel.setPassword(expectedPassword);

        assertEquals(expectedPassword, userModel.getPassword());
    }

    @Test
    public void testSetPasswordCheck()
    {
        String userName = "George";
        UserModel userModel = new UserModel(userName);
        String expectedPassword = "Cyber_Security";
        userModel.setPasswordCheck(expectedPassword);

        assertEquals(expectedPassword, userModel.getPasswordCheck());
    }

    @Test
    public void testToJSONString()
    {
        String expectedUserName = "Ozhan";
        UserModel userModel = new UserModel(expectedUserName);
        String expectedPassword = "Security123";
        userModel.setPassword(expectedPassword);

        String expectedString = String.format("{\"password\":\"%s\",\"username\":\"%s\"}", expectedPassword, expectedUserName);
        assertEquals(expectedString, userModel.toJSONString());

    }

}
