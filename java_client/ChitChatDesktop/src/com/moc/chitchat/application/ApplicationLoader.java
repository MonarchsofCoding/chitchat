package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.RegistrationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ApplicationLoader provides everything to do with loading the application.
 */
@Component
public class ApplicationLoader {

    private SplashWindow splashWindow;
    private RegistrationView registrationView;

    /**
     * Constructor for the ApplicationLoader.
     * @param splashWindow the splash window.
     * @param registrationView The view for Registration.
     */
    @Autowired
    ApplicationLoader(
        SplashWindow splashWindow,
        RegistrationView registrationView
    ) {
        this.splashWindow = splashWindow;
        this.registrationView = registrationView;
    }

    /**
     * load: Sets up the application.
     */
    public void load() {
        this.splashWindow.setVisible(true);
        // Do loading things...

        this.splashWindow.dispose();

        // Show Registration/Login Window
        this.registrationView.setVisible(true);
    }
}
