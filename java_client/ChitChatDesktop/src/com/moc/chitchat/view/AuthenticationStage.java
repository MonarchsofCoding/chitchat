package com.moc.chitchat.view;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationStage extends Stage {

    private Scene loginScene;
    private Scene registrationScene;

    /**
     * Construction for the AuthenticationStage.
     * @param loginView - The login in view
     * @param registrationView - The registration view
     */
    @Autowired
    public AuthenticationStage(
            LoginView loginView,
            RegistrationView registrationView
    ) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX / 2);
        int height = Math.round(resY / 2);

        this.setX((resX - width) / 2);
        this.setY((resY - height) / 2);
        loginView.setStage(this);
        loginView.setWidth(width);
        loginView.setHeight(height);

        this.loginScene = loginView.getScene();
        registrationView.setStage(this);
        registrationView.setWidth(width);
        registrationView.setHeight(height);
        this.registrationScene = registrationView.getScene();


        this.setTitle(" Registration User's Screen");

        this.showLogin();
    }

    public void showLogin() {
        this.setScene(this.loginScene);
        this.setTitle("Login User's Screen");
    }

    void showRegister() {
        this.setScene(this.registrationScene);
    }
}
