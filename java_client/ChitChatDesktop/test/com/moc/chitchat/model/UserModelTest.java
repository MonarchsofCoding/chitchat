package com.moc.chitchat.model;

import com.moc.chitchat.crypto.CryptoFunctions;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

/**
 * UserModelTest provides tests for the UserModel
 */
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
    public void testToJSONStringwithPublicKey() throws Exception {
        String expectedUserName = "Ozhan";
        UserModel userModel = new UserModel(expectedUserName);
        String expectedPassword = "Security123";
        userModel.setPassword(expectedPassword);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair pair = cryptoFunctions.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        userModel.setPublicKey(publicKey);


        String expectedString = String
                .format(
                        "{\"public_key\":\"%s\",\"password\":\"%s\",\"username\":\"%s\"}",
                        Base64.getEncoder().encodeToString(publicKey.getEncoded()),
                        expectedPassword, expectedUserName);
        assertEquals(expectedString, userModel.toJSONString());
    }

    @Test
    public void testToJSONString() throws Exception {
        String expectedUserName = "Ozhan";
        UserModel userModel = new UserModel(expectedUserName);
        String expectedPassword = "Security123";
        userModel.setPassword(expectedPassword);

        String expectedString = String
                .format(
                        "{\"password\":\"%s\",\"username\":\"%s\"}",
                        expectedPassword,
                        expectedUserName)
                ;
        assertEquals(expectedString, userModel.toJSONString());
    }

    @Test
    public void setAuthToken() {
        String authToken = "some_String";
        String name = "halo";
        UserModel userModel = new UserModel(name);
        userModel.setAuthToken(authToken);

        assertEquals(authToken, userModel.getAuthToken());
    }


}
