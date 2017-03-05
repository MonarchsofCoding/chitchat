package com.moc.chitchat.application;

import com.moc.chitchat.view.BaseStage;
import java.util.List;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ApplicationLoader provides everything to do with loading the application.
 */
@Component
public class ApplicationLoader {

    private Configuration configuration;
    private BaseStage baseStage;

    /**
     * Constructor for the ApplicationLoader.
     * @param configuration The configuration for the application.
     */
    @Autowired
    public ApplicationLoader(
        Configuration configuration,
        BaseStage baseStage
    ) {
        this.configuration = configuration;
        this.baseStage = baseStage;
    }

    /**
     * load: Sets up the application.
     */
    public void load(Stage stage, List<String> args) {
        if (args.size() > 0 && args.get(0).equals("dev")) {
            this.configuration.setDevelopmentMode();
        } else if (args.size() > 0 && args.get(0).equals("test")) {
            this.configuration.setTestingMode();
        } else if (args.size() > 0 && args.get(0).equals("beta")) {
            this.configuration.setBetaMode();
        }

        // Show Authentication
        this.baseStage.setPrimaryStage(stage);
        this.baseStage.show();
    }
}
