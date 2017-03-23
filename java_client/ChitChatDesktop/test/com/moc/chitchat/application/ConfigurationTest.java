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

        assertEquals(configuration.getBackendAddress(), "http://chitchat:4000");
    }

    @Test
    public void testAlphaMode() {
        Configuration configuration = new Configuration();

        configuration.setAlphaMode();

        assertEquals(configuration.getBackendAddress(), "https://alpha.chitchat.monarchsofcoding.com");
    }

    @Test
    public void testSetBetaMode() {
        Configuration configuration = new Configuration();

        configuration.setBetaMode();

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

    @Test
    public void testLogout() {
        Configuration configuration = new Configuration();

        UserModel userModel = new UserModel("Madara");
        configuration.setLoggedInUser(userModel);
        // Showing that the user was logged in
        assertEquals("Madara", configuration.getLoggedInUser().getUsername());

        // Test
        configuration.logout();
        assertEquals(null, configuration.getLoggedInUser());
    }
}
