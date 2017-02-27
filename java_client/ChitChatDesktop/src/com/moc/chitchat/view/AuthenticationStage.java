package com.moc.chitchat.view;

import com.moc.chitchat.view.main.MainStage;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationStage {

    private Scene loginScene;
    private Scene registrationScene;

    private LoginView loginView;
    private RegistrationView registrationView;

    private Stage primaryStage;
    private MainStage mainStage;

    /**
     * Construction for the AuthenticationStage.
     * @param mainStage - The main primaryStage
     * @param loginView - The login in view
     * @param registrationView - The registration view
     */
    @Autowired
    public AuthenticationStage(
            MainStage mainStage,
            LoginView loginView,
            RegistrationView registrationView
    ) {
        this.mainStage = mainStage;
        this.loginView = loginView;
        this.registrationView = registrationView;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * shows the authentication scene.
     */
    public void show() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX / 2);
        int height = Math.round(resY / 2);

        this.primaryStage.setX((resX - width) / 2);
        this.primaryStage.setY((resY - height) / 2);

        this.loginView.setAuthenticationStage(this);
        this.loginView.setWidth(width);
        loginView.setHeight(height);

        this.loginScene = loginView.getScene();
        registrationView.setAuthenticationStage(this);
        registrationView.setWidth(width);
        registrationView.setHeight(height);
        this.registrationScene = registrationView.getScene();

        this.showLogin();
        this.primaryStage.show();
    }


    void showLogin() {
        this.primaryStage.setScene(this.loginScene);
        this.primaryStage.setTitle("Chit Chat - Login");
    }

    void showRegister() {
        this.primaryStage.setScene(this.registrationScene);
        this.primaryStage.setTitle("Chit Chat - Registration");
    }

    void showMainStage() {
        this.mainStage.setPrimaryStage(this.primaryStage);
        this.mainStage.show();
    }
}
