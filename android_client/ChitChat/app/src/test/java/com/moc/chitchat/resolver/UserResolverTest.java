package com.moc.chitchat.resolver;

import com.moc.chitchat.model.UserModel;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * UserResolverTest provides the tests for the User Resolver
 */

public class UserResolverTest {

    @Test
    public void testCreateUser() {
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
}
