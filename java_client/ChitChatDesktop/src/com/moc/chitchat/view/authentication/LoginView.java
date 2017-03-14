package com.moc.chitchat.view.authentication;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.moc.chitchat.controller.authentication.LoginController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.BaseStage;
import com.moc.chitchat.view.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.awt.*;

/**
 * Provides the Login view interface.
 */
@Component
public class LoginView extends BaseView implements EventHandler<ActionEvent> {

    private LoginController loginController;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button registerBtn;
    private Label unexpectedErrors;

    @Autowired
    LoginView(
            LoginController loginController
    ) {
        this.loginController = loginController;
    }

    @Override
    public MigPane getContentPane() {
        this.baseStage.setWindowTitle("Chit Chat - Login");
        this.usernameField = new JFXTextField();
        this.usernameField.setId("login-username-fld");
        this.usernameField.setPromptText("Username");
        this.usernameField.setMinWidth(295.0); // 295.0 is the magic width
        MigPane loginForm = new MigPane();
        loginForm.add(this.usernameField,"grow,wrap");

        this.passwordField = new JFXPasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setId("login-password-fld");
        this.passwordField.setOnAction(this);
        this.passwordField.setMinWidth(295.0); // 295.0 is the magic width
        loginForm.add(this.passwordField,"grow,wrap");

        this.loginBtn = new JFXButton("Login");
        this.loginBtn.setOnAction(this);
        this.loginBtn.setId("login-login-btn");
        this.loginBtn.setStyle("-fx-font: 14 arial; -fx-base: #9bc2cf");
        this.loginBtn.setMinWidth(295.0); // 295.0 is the magic width
        loginForm.add(this.loginBtn, "wrap, grow");

        this.registerBtn = new JFXButton("Register");
        this.registerBtn.setId("login-register-btn");
        this.registerBtn.setOnAction(this);
        this.registerBtn.setStyle("-fx-font: 14 arial; -fx-base: #b5dbe8");
        this.registerBtn.setMinWidth(295.0); // 295.0 is the magic width
        loginForm.add(this.registerBtn, "wrap,grow");

        this.unexpectedErrors = new Label();
        this.unexpectedErrors.setId("login-errors-lbl");
        this.unexpectedErrors.setTextFill(Color.RED);
        this.unexpectedErrors.setVisible(false);

        loginForm.add(this.unexpectedErrors,"wrap");

        MigPane loginPane = new MigPane();
        loginPane.setLayout("fill");
        loginPane.add(loginForm, "span, split 2, center");
        loginPane.setId("login-view-pane");

        return loginPane;
    }

    private void loginAction() {
        try {
            UserModel user = this.loginController.loginUser(
                    this.usernameField.getText(),
                    this.passwordField.getText()
            );
            // Clearing the fields
            this.usernameField.clear();
            this.passwordField.clear();
            this.baseStage.showMainView();

            //  JOptionPane.showMessageDialog(frame,
            // String.format("Success! You have now registered %s!", user.getUsername()));
        } catch (ValidationException validationException) {
            Errors errors = validationException.getErrors();
            if (errors.hasErrors()) {
                this.unexpectedErrors.setText("Wrong credentials or you have not registered yet !");
                this.unexpectedErrors.setVisible(true);
            }

        } catch (Exception exception) {

            this.unexpectedErrors.setText("Unexpected error from server");
            this.unexpectedErrors.setVisible(true);
            exception.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.passwordField || actionEvent.getSource() == this.loginBtn) {
            this.unexpectedErrors.setVisible(false);
            this.loginAction();
        } else if (actionEvent.getSource() == this.registerBtn) {
            this.unexpectedErrors.setText("");
            this.usernameField.setText("");
            this.passwordField.setText("");
            this.baseStage.showRegister();
        }
    }
}

