package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


/**
 * ConfigurationTest provides tests for the Configuration class.
 */
public class ConfigurationTest {

    @Test
    public void testConstructor() {
        Configuration configuration = new Configuration();

        assertEquals(configuration.getBackendAddress(), "https://chitchat.monarchsofcoding.com");
    }

    @Test
    public void testSetDevelopmentMode() {
        Configuration configuration = new Configuration();

        configuration.setDevelopmentMode();

        assertEquals(configuration.getBackendAddress(), "http://localhost:4000");
    }

    @Test
    public void testSetTestingMode() {
        Configuration configuration = new Configuration();

        configuration.setTestingMode();

        assertEquals(configuration.getBackendAddress(), "https://beta.chitchat.monarchsofcoding.com");
    }

    @Test
    public void testGetLoggedInUser() {
        Configuration configuration = new Configuration();

        assertEquals(null, configuration.getLoggedInUser());
    }

    @Test
    public void testSetLoggedInUser() {
        Configuration configuration = new Configuration();

        UserModel mockUser = mock(UserModel.class);

        configuration.setLoggedInUser(mockUser);

        assertEquals(mockUser, configuration.getLoggedInUser());
    }
}
