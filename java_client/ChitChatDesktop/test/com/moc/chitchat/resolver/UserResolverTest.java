package com.moc.chitchat.resolver;

import com.moc.chitchat.crypto.CryptoFunctions;
import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.junit.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * UserResolverTest provides tests for the UserResolver
 */
public class UserResolverTest {

    @Test
    public void testCreateUser()
    {
        String expectedUsername = "Greg";
        String expectedPassword = "Bear";
        String expectedPasswordCheck = "Bear";

        UserResolver userResolver = new UserResolver();

        UserModel user = userResolver.createUser(expectedUsername, expectedPassword, expectedPasswordCheck);

        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
        assertEquals(expectedPasswordCheck, user.getPasswordCheck());
    }

    @Test
    public void testCreateUserName()
    {
        String expectedUsername = "Wellbeing";

        UserResolver userResolver = new UserResolver();
        UserModel user = userResolver.createUser(expectedUsername);

        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    public void testCreateLoginUser() throws Exception {
        String expectedUsename = "Vjftw";
        String expectedPasswordlgn = "aaaaaaaa";

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        UserResolver userResolver = new UserResolver();
        UserModel user = userResolver
                .createUser(expectedUsename,
                            expectedPasswordlgn,
                            userKeyPair.getPublic(),
                            userKeyPair.getPrivate())
                ;

        assertEquals(expectedUsename,user.getUsername());
        assertEquals(expectedPasswordlgn,user.getPassword());
        assertEquals(userKeyPair.getPublic(), user.getPublicKey());
        assertEquals(userKeyPair.getPrivate(), user.getPrivatekey());
    }

    @Test
    public void testGetUserModelViaJSonObject() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "bob_dillion");
        jsonObject.put("public_key", cryptoFunctions.keyToString(userKeyPair.getPublic()));

        UserResolver userResolver = new UserResolver();
        UserModel userModel = userResolver.getUserModelViaJSonObject(jsonObject);

        assertEquals("bob_dillion", userModel.getUsername());
        assertEquals(userKeyPair.getPublic(), userModel.getPublicKey());
    }

}
