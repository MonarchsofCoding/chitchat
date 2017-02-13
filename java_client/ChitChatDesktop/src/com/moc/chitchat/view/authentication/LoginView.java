package com.moc.chitchat.view.authentication;

import com.moc.chitchat.controller.authentication.LoginController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.tbee.javafx.scene.layout.fxml.MigPane;


@Component
public class LoginView implements EventHandler<ActionEvent> {

    private LoginController loginController;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginBtn;

    @Autowired
    LoginView(
        LoginController loginController
    ) {
        this.loginController = loginController;
    }

    public MigPane buildContentPane() {
        MigPane loginPane = new MigPane();

        loginPane.setStyle("-fx-background-color: #CCC;");

        MigPane loginForm = new MigPane();
        loginForm.setStyle("-fx-alignment: center; -fx-background-color: #EEE;");

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Username");
        loginForm.add(this.usernameField, "span 6, wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setOnAction(this);
        loginForm.add(this.passwordField, "span 6, wrap");

        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        loginForm.add(this.loginBtn, "wrap, grow");

        loginPane.add(loginForm, "dock west");

        return loginPane;
    }

    private void loginAction() {
        try {
            UserModel user = this.loginController.loginUser(
                this.usernameField.getText(),
                this.passwordField.getText()
            );
          //  JOptionPane.showMessageDialog(frame, String.format("Success! You have now registered %s!", user.getUsername()));
        } catch (ValidationException e) {
            Errors errors = e.getErrors();

            if (errors.getFieldError().getField().equals(("username")))
            {
//                JOptionPane.showMessageDialog(frame, "Username " + errors.getFieldError("username").getDefaultMessage());
            }

            if (errors.getFieldError().getField().equals("password"))
            {
//                JOptionPane.showMessageDialog(frame, "Password " + errors.getFieldError("password").getDefaultMessage());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.passwordField || actionEvent.getSource() == this.loginBtn) {
            this.loginAction();
        }
    }
}

