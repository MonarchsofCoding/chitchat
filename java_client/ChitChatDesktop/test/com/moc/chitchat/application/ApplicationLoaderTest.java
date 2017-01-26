package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.RegistrationView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * ApplicationLoaderTest provides tests for ApplicationLoader
 */
public class ApplicationLoaderTest {

    @Test
    public void testConstructor() {
        SplashWindow mockSplashWindow = mock(SplashWindow.class);
        RegistrationView mockRegistrationView = mock(RegistrationView.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView);

        assertNotNull(applicationLoader);
        assertEquals(applicationLoader.getClass(), ApplicationLoader.class);
    }

    @Test
    public void testLoad() {
        SplashWindow mockSplashWindow = mock(SplashWindow.class);
        RegistrationView mockRegistrationView = mock(RegistrationView.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView);

        applicationLoader.load();

        verify(mockSplashWindow).setVisible(true);
    }

}
