package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.AuthenticationStage;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ApplicationLoader provides everything to do with loading the application.
 */
@Component
public class ApplicationLoader {

    private Configuration configuration;
    private AuthenticationStage authenticationStage;

    /**
     * Constructor for the ApplicationLoader.
     * @param configuration The configuration for the application.
     */
    @Autowired
    ApplicationLoader(
        Configuration configuration,
        AuthenticationStage authenticationStage
    ) {
        this.configuration = configuration;
        this.authenticationStage = authenticationStage;
    }

    /**
     * load: Sets up the application.
     */
    public void load(Stage stage, List<String> args) {
        if (args.size() > 0) {
            switch (args.get(0)) {
                case "dev":
                    this.configuration.setDevelopmentMode();
                    break;

                case "test":
                    this.configuration.setTestingMode();
                    break;
            }
        }

        // AuthenticationStage
        this.authenticationStage.showAndWait();

        System.out.println("Authentication Finished!");
    }
}
