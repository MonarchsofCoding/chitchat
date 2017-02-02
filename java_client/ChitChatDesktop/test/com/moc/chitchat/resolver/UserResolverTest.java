package com.moc.chitchat.resolver;

import com.moc.chitchat.model.UserModel;
//import com.moc.chitchat.resolver.UserResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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
}
