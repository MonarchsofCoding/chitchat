package com.moc.chitchat.application;

import org.springframework.stereotype.Component;

import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JWindow;

/**
 * SplashWindow provides a loading window to display to the User while the application loads.
 */
@Component
class SplashWindow extends JWindow {

    /**
     * Constructor for the SplashWindow.
     */
    SplashWindow() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        this.setSize(resX/3, resY/3);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(51, 51, 51));
        this.setVisible(false);
    }
}
