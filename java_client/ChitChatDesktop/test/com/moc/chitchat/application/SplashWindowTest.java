package com.moc.chitchat.application;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

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

    @Test
    public void testSplashWindowLocationCenter() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        SplashWindow splashWindow = new SplashWindow();
        Dimension splashWindowSize = splashWindow.getSize();

        double expectedX = width/2 - (splashWindowSize.getWidth()/2);
        double expectedY = height/2 - (splashWindowSize.getHeight()/2);

//        assertEquals(expectedX, splashWindow.getLocation().getX(), 5);

//        assertEquals(
//            expectedY,
//            splashWindow.getLocation().getY(),
//            2
//        );
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

        assertEquals(false, splashWindow.isVisible());
    }

}
