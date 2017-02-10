package com.moc.chitchat;

import com.moc.chitchat.application.ApplicationLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Class Application provides the entry point for the application.
 */
@Configuration
@ComponentScan
public class Application {

    /**
     * main provides the entry point for this application.
     * @param args The environment to run. e.g. `java -jar app.jar dev`.
     *             Defaults to `prod`. Modes: `dev`, `test`, `prod`.
     */
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        System.out.println(context.getApplicationName());

        ApplicationLoader applicationLoader = context.getBean(ApplicationLoader.class);
        applicationLoader.load(args);
    }
}
