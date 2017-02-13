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
    private Configuration configuration;

    /**
     * Constructor for the ApplicationLoader.
     * @param splashWindow the splash window.
     * @param registrationView The view for Registration.
     * @param configuration The configuration for the application.
     */
    @Autowired
    ApplicationLoader(
        SplashWindow splashWindow,
        RegistrationView registrationView,
        Configuration configuration
    ) {
        this.splashWindow = splashWindow;
        this.registrationView = registrationView;
        this.configuration = configuration;
    }

    /**
     * load: Sets up the application.
     */
    public void load(String[] args) {
        this.splashWindow.setVisible(true);
        // Do loading things...

        if (args.length > 0) {
            switch (args[0]) {
                case "dev": this.configuration.setDevelopmentMode();
                            break;

                case "test": this.configuration.setTestingMode();
                            break;
            }
        }

        this.splashWindow.dispose();

        // Show Registration/Login Window
        this.registrationView.setVisible(true);
    }
}
