package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.LoginView;
import com.moc.chitchat.view.authentication.RegistrationView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ApplicationLoader provides everything to do with loading the application.
 */
@Component
public class ApplicationLoader {

    private SplashWindow splashWindow;
    private Configuration configuration;
    private LoginView loginView;
    
    /**
     * Constructor for the ApplicationLoader.
     * @param splashWindow the splash window.
     * @param loginView The view for Login
     * @param configuration The configuration for the application.
     */
    @Autowired
    ApplicationLoader(
        SplashWindow splashWindow,
        LoginView loginView,
        Configuration configuration
    ) {
        this.splashWindow = splashWindow;
        this.loginView = loginView;
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
                case "dev":
                    this.configuration.setDevelopmentMode();
                    break;

                case "test":
                    this.configuration.setTestingMode();
                    break;
            }
        }

        this.splashWindow.dispose();

        // Show Registration/Login Window
        this.loginView.setVisible(true);
    }
}
