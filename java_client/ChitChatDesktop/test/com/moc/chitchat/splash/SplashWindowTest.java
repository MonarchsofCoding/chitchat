package com.moc.chitchat.splash;

import static org.junit.Assert.*;
import org.junit.*;

import java.awt.*;

/**
 * SplashWindowTest provides tests for SplashWindow
 */
public class SplashWindowTest {

    @Test
    public void testSplashWindowWidth() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();

        SplashWindow splashWindow = new SplashWindow();

        assertEquals(resX/3, splashWindow.getSize().getWidth(), 0);
    }

    @Test
    public void testSplashWindowHeight() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resY = gd.getDisplayMode().getHeight();

        SplashWindow splashWindow = new SplashWindow();

        assertEquals(resY/3, splashWindow.getSize().getHeight(), 0);
    }

    @Test()
    public void testSplashWindowBackground() {
        SplashWindow splashWindow = new SplashWindow();

        Color splashWindowBackground = splashWindow.getContentPane().getBackground();

        assertEquals(splashWindowBackground.getRed(), 51);
        assertEquals(splashWindowBackground.getGreen(), 51);
        assertEquals(splashWindowBackground.getBlue(), 51);
    }

    @Test()
    public void testSplashWindowVisible() {
        SplashWindow splashWindow = new SplashWindow();

        assertEquals(true, splashWindow.isVisible());
    }

}
