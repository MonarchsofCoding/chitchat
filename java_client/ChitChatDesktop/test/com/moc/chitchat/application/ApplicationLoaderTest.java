package com.moc.chitchat.application;


import com.moc.chitchat.view.BaseStage;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * ApplicationLoaderTest provides tests for ApplicationLoader
 */
public class ApplicationLoaderTest {

    @Test
    public void testConstructor() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        assertNotNull(applicationLoader);
        assertEquals(applicationLoader.getClass(), ApplicationLoader.class);
    }

    @Test
    public void testLoadDefaultProdEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        Stage mockStage = mock(Stage.class);
        List<String> args = new ArrayList<>();

        applicationLoader.load(mockStage, args);

        verify(mockBaseStage).setPrimaryStage(mockStage);
        verify(mockBaseStage).show();
    }

    @Test
    public void testLoadTestEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        Stage mockStage = mock(Stage.class);
        List<String> args = new ArrayList<>();
        args.add("test");

        applicationLoader.load(mockStage, args);

        verify(mockConfiguration).setTestingMode();

        verify(mockBaseStage).setPrimaryStage(mockStage);
        verify(mockBaseStage).show();
    }

    @Test
    public void testLoadDevelopmentEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        Stage mockStage = mock(Stage.class);
        List<String> args = new ArrayList<>();
        args.add("dev");

        applicationLoader.load(mockStage, args);

        verify(mockConfiguration).setDevelopmentMode();

        verify(mockBaseStage).setPrimaryStage(mockStage);
        verify(mockBaseStage).show();
    }

    @Test
    public void testLoadBetaEnvironment() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        Stage mockStage = mock(Stage.class);
        List<String> args = new ArrayList<>();
        args.add("beta");

        applicationLoader.load(mockStage, args);

        verify(mockConfiguration).setBetaMode();

        verify(mockBaseStage).setPrimaryStage(mockStage);
        verify(mockBaseStage).show();
    }

    @Test
    public void testLoadProdByDefaultWithUnknownArg() {
        Configuration mockConfiguration = mock(Configuration.class);
        BaseStage mockBaseStage = mock(BaseStage.class);
        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockBaseStage);

        Stage mockStage = mock(Stage.class);
        List<String> args = new ArrayList<>();
        args.add("a");

        applicationLoader.load(mockStage, args);


        verify(mockBaseStage).setPrimaryStage(mockStage);
        verify(mockBaseStage).show();
    }
}
