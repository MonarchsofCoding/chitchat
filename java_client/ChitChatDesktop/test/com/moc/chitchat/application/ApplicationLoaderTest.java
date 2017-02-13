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
        Configuration mockConfiguration = mock(Configuration.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView, mockConfiguration);

        assertNotNull(applicationLoader);
        assertEquals(applicationLoader.getClass(), ApplicationLoader.class);
    }

    @Test
    public void testLoadDefaultProdEnvironment() {
        SplashWindow mockSplashWindow = mock(SplashWindow.class);
        RegistrationView mockRegistrationView = mock(RegistrationView.class);
        Configuration mockConfiguration = mock(Configuration.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView, mockConfiguration);

        String[] args = {};

        applicationLoader.load(args);

        verify(mockSplashWindow).setVisible(true);
    }

    @Test
    public void testLoadTestEnvironment() {
        SplashWindow mockSplashWindow = mock(SplashWindow.class);
        RegistrationView mockRegistrationView = mock(RegistrationView.class);
        Configuration mockConfiguration = mock(Configuration.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView, mockConfiguration);

        String[] args = {"test"};

        applicationLoader.load(args);

        verify(mockSplashWindow).setVisible(true);
    }

    @Test
    public void testLoadDevEnvironment() {
        SplashWindow mockSplashWindow = mock(SplashWindow.class);
        RegistrationView mockRegistrationView = mock(RegistrationView.class);
        Configuration mockConfiguration = mock(Configuration.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockSplashWindow, mockRegistrationView, mockConfiguration);

        String[] args = {"dev"};

        applicationLoader.load(args);

        verify(mockSplashWindow).setVisible(true);
    }

}
