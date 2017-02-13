package com.moc.chitchat.application;

import com.moc.chitchat.view.MainView;
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
    private MainView mainView;

    /**
     * Constructor for the ApplicationLoader.
     * @param configuration The configuration for the application.
     */
    @Autowired
    ApplicationLoader(
        MainView mainView,
        Configuration configuration
    ) {
        this.mainView = mainView;
        this.configuration = configuration;
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
        this.mainView.load(stage);
    }
}
