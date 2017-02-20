package com.moc.chitchat.application;

import com.moc.chitchat.view.authentication.AuthenticationStage;
import com.moc.chitchat.view.main.MainStage;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * ApplicationLoader provides everything to do with loading the application.
 */
@Component
public class ApplicationLoader {

    private Configuration configuration;
    private AuthenticationStage authenticationStage;
    private MainStage mainStage;

    /**
     * Constructor for the ApplicationLoader.
     * @param configuration The configuration for the application.
     */
    @Autowired
    ApplicationLoader(
        Configuration configuration,
        AuthenticationStage authenticationStage,
        MainStage mainStage
    ) {
        this.configuration = configuration;
        this.authenticationStage = authenticationStage;
        this.mainStage = mainStage;
    }

    /**
     * load: Sets up the application.
     */
    public void load(Stage stage, List<String> args) {
        if (args.size() > 0 && args.get(0).equals("dev")) {
            this.configuration.setDevelopmentMode();
        } else if (args.size() > 0 && args.get(0).equals("test")) {
            this.configuration.setTestingMode();
        }

        // AuthenticationStage
        this.authenticationStage.showAndWait();

        if(this.configuration.getLoggedInUser()!=null){
            this.mainStage.showAndWait();
        }

    }
}
