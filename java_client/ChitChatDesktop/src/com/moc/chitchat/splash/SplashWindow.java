package com.moc.chitchat.splash;

import javax.swing.*;
import java.awt.*;

/**
 * SplashWindow provides a loading window to display to the User while the application loads.
 */
public class SplashWindow extends JWindow {

    /**
     *
     */
    public SplashWindow() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        this.setSize(resX/3, resY/3);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(new Color(51, 51, 51));
        this.setVisible(true);
    }
}
