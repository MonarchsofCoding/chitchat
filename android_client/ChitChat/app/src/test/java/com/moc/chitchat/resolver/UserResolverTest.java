package com.moc.chitchat.resolver;

import com.moc.chitchat.model.UserModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * UserResolverTest provides the tests for the User Resolver
 */

public class UserResolverTest {

    @Test
    public void testCreateReginsterUser() {
        String expectedusername = "SuperFood";
        String expectedpassword = "poiuytrewq";
        String expectedpasswordCheck = "poiuytrewq";

        UserResolver userResolver = new UserResolver();

        UserModel user = userResolver.createRegisterUser(expectedusername,
            expectedpassword,
            expectedpasswordCheck);

        assertEquals(expectedusername, user.getUsername());
        assertEquals(expectedpassword, user.getPassword());
        assertEquals(expectedpasswordCheck, user.getPasswordCheck());
    }

    @Test
    public void testCreateLoginUser() {
        String expectedusername = "SuperFood";
        String expectedpassword = "poiuytrewq";


        UserResolver userResolver = new UserResolver();

        UserModel user = userResolver.createLoginUser(expectedusername,
            expectedpassword);

        assertEquals(expectedusername, user.getUsername());
        assertEquals(expectedpassword, user.getPassword());
    }
}
