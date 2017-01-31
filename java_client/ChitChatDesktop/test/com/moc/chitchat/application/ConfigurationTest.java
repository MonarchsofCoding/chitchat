package com.moc.chitchat.application;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * ConfigurationTest provides tests for the Configuration class.
 */
public class ConfigurationTest {

    @Test
    public void testConstructor() {
        Configuration configuration = new Configuration();

        assertEquals(configuration.getBackendAddress(), "https://chitchat.moc.com");
    }

    @Test
    public void testSetDevelopmentMode() {
        Configuration configuration = new Configuration();

        configuration.setDevelopmentMode();

        assertEquals(configuration.getBackendAddress(), "http://localhost");
    }

    @Test
    public void testSetTestingMode() {
        Configuration configuration = new Configuration();

        configuration.setTestingMode();

        assertEquals(configuration.getBackendAddress(), "https://beta.chitchat.moc.com");
    }
}
