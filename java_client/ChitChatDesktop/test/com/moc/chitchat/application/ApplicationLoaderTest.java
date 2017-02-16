package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.AuthenticationStage;
import com.moc.chitchat.view.authentication.LoginView;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        Configuration mockConfiguration = mock(Configuration.class);
        AuthenticationStage mockAuthenticationStage = mock(AuthenticationStage.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage);

        assertNotNull(applicationLoader);
        assertEquals(applicationLoader.getClass(), ApplicationLoader.class);
    }

    @Test
    public void testLoadDefaultProdEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        AuthenticationStage mockAuthenticationStage = mock(AuthenticationStage.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage);

        Stage mockStage = mock(Stage.class);

        List<String> args = new ArrayList<>();

        applicationLoader.load(mockStage, args);

        verify(mockAuthenticationStage).showAndWait();
    }

    @Test
    public void testLoadTestEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        AuthenticationStage mockAuthenticationStage = mock(AuthenticationStage.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage);

        Stage mockStage = mock(Stage.class);

        List<String> args = new ArrayList<>();
        args.add("test");

        applicationLoader.load(mockStage, args);

        verify(mockConfiguration).setTestingMode();

        verify(mockAuthenticationStage).showAndWait();
    }

    @Test
    public void testLoadDevEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        AuthenticationStage mockAuthenticationStage = mock(AuthenticationStage.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage);

        Stage mockStage = mock(Stage.class);

        List<String> args = new ArrayList<>();
        args.add("dev");

        applicationLoader.load(mockStage, args);

        verify(mockConfiguration).setDevelopmentMode();

        verify(mockAuthenticationStage).showAndWait();
    }

    @Test
    public void testLoadProdByDefaultWithUnknownArg() {
        Configuration mockConfiguration = mock(Configuration.class);
        AuthenticationStage mockAuthenticationStage = mock(AuthenticationStage.class);

        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage);

        Stage mockStage = mock(Stage.class);

        List<String> args = new ArrayList<>();
        args.add("aaa");

        applicationLoader.load(mockStage, args);

        verify(mockAuthenticationStage).showAndWait();
    }

}
