package com.moc.chitchat;

import com.moc.chitchat.application.ApplicationLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Class Application provides the entry point for the application.
 */
@Configuration
@ComponentScan
public class Main extends Application {

    /**
     * main provides the entry point for this application.
     * @param args The environment to run. e.g. `java -jar app.jar dev`.
     *             Defaults to `prod`. Modes: `dev`, `test`, `prod`.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(true);
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        ApplicationLoader applicationLoader = context.getBean(ApplicationLoader.class);

        applicationLoader.load(primaryStage, this.getParameters().getUnnamed());
    }
}
