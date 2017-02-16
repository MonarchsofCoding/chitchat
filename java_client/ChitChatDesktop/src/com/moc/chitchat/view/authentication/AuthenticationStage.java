package com.moc.chitchat.view.authentication;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class AuthenticationStage extends Stage {

    private Scene loginScene;
    private Scene registrationScene;
    private Scene MainChatScene;


    @Autowired
    public AuthenticationStage(
        LoginView loginView,
        RegistrationView registrationView
    ) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX/2);
        int height = Math.round(resY/2);

        this.setX((resX - width)/2);
        this.setY((resY - height)/2);

        loginView.setStage(this);
        loginView.setWidth(width);
        loginView.setHeight(height);
        this.loginScene = loginView.getScene();
        registrationView.setStage(this);
        registrationView.setWidth(width);
        registrationView.setHeight(height);
        this.registrationScene = registrationView.getScene();



        this.setTitle("Chit Chat Application");

        this.showLogin();
    }

    public void showLogin() {
        this.setScene(this.loginScene);
    }

    void showRegister() {
        this.setScene(this.registrationScene);
    }
}
