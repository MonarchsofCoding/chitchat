package com.moc.chitchat.application;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * ApplicationLoaderTest provides tests for ApplicationLoader
 */
public class ApplicationLoaderTest {

//    @Test
//    public void testConstructor() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage,mockMainStage);
//
//        applicationLoader = new ApplicationLoader(
//
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//        assertNotNull(applicationLoader);
//        assertEquals(applicationLoader.getClass(), ApplicationLoader.class);
//    }
//
//    @Test
//    public void testLoadDefaultProdEnvironment() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//
//
//        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage,mockMainStage);
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        Stage mockStage = mock(Stage.class);
//        List<String> args = new ArrayList<>();
//
//        applicationLoader.load(mockStage, args);
//
//        verify(mockAuthenticationStage).showAndWait();
//    }
//
//    @Test
//    public void testLoadTestEnvironment() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//
//
//
//        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage,mockMainStage);
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        Stage mockStage = mock(Stage.class);
//
//        List<String> args = new ArrayList<>();
//        args.add("test");
//
//        applicationLoader.load(mockStage, args);
//
//        verify(mockConfiguration).setTestingMode();
//
//        verify(mockAuthenticationStage).showAndWait();
//    }
//
//    @Test
//    public void testLoadDevEnvironment() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//
//
//
//        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage,mockMainStage);
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        Stage mockStage = mock(Stage.class);
//
//        List<String> args = new ArrayList<>();
//        args.add("dev");
//        applicationLoader.load(mockStage, args);
//
//        verify(mockConfiguration).setDevelopmentMode();
//
//        verify(mockAuthenticationStage).showAndWait();
//    }
//
//    @Test
//    public void testLoadProdByDefaultWithUnknownArg() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//
//
//
//        ApplicationLoader applicationLoader = new ApplicationLoader(mockConfiguration, mockAuthenticationStage,mockMainStage);
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//        when(mockConfiguration.getLoggedInUser()).thenReturn(null);
//
//        applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//
//        Stage mockStage = mock(Stage.class);
//
//        List<String> args = new ArrayList<>();
//        args.add("aaa");
//
//        applicationLoader.load(mockStage, args);
//
//        verify(mockAuthenticationStage).showAndWait();
//    }
//
//    @Test
//    public void testLoadMainStageWhenLoggedIn() {
//        Configuration mockConfiguration = mock(Configuration.class);
//        BaseStage mockAuthenticationStage = mock(BaseStage.class);
//        MainStage mockMainStage = mock(MainStage.class);
//
//        UserModel mockUser = mock(UserModel.class);
//        when(mockConfiguration.getLoggedInUser()).thenReturn(mockUser);
//
//        ApplicationLoader applicationLoader = new ApplicationLoader(
//                mockConfiguration,
//                mockAuthenticationStage,
//                mockMainStage
//        );
//
//        Stage mockStage = mock(Stage.class);
//
//        List<String> args = new ArrayList<>();
//
//        applicationLoader.load(mockStage, args);
//
//        verify(mockAuthenticationStage).showAndWait();
//        verify(mockMainStage).showAndWait();
//
//    }

}
