package com.moc.chitchat.view.authentication;

import com.moc.chitchat.application.Configuration;
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
public class LoginView extends BaseView implements EventHandler<ActionEvent> {

    private LoginController loginController;

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button registerBtn;

    private AuthenticationStage stage;

    @Autowired
    LoginView(
        LoginController loginController
    ) {
        this.loginController = loginController;
    }

    public void setStage(AuthenticationStage stage) {
        this.stage = stage;
    }

    @Override
    public MigPane getContentPane() {
        MigPane loginPane = new MigPane();

        MigPane loginForm = new MigPane();

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Username");
        loginForm.add(this.usernameField, "span, wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setOnAction(this);
        loginForm.add(this.passwordField, "span, wrap");

        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        loginForm.add(this.loginBtn, "wrap, grow");

        this.registerBtn = new Button("Register");
        this.registerBtn.setOnAction(this);
        loginForm.add(this.registerBtn, "wrap, grow");

        loginPane.add(loginForm, "span, split 2, center");

        return loginPane;
    }

    private void loginAction() {
        try {
            UserModel user = this.loginController.loginUser(
                this.usernameField.getText(),
                this.passwordField.getText()
            );

            this.stage.hide();


          //  JOptionPane.showMessageDialog(frame, String.format("Success! You have now registered %s!", user.getUsername()));
        } catch (ValidationException e) {
            Errors errors = e.getErrors();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Unsuccesfull Login");
            if(errors.hasErrors()) {
                if (errors.getFieldError().getField().equals(("username"))) {
                    alert.setContentText("Username " + errors.getFieldError("username").getDefaultMessage());
                    alert.show();
                }
                if (errors.getFieldError().getField().equals("password")) {
                    alert.setContentText("Password " + errors.getFieldError("password").getDefaultMessage());
                    alert.show();
                }
            }

                    alert.setContentText("Wrong credentials or you have not registered yet !");
                    alert.show();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Unexpected error from server");
            alert.show();
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.passwordField || actionEvent.getSource() == this.loginBtn) {
            this.loginAction();
        } else if (actionEvent.getSource() == this.registerBtn) {
            this.stage.showRegister();
        }
    }
}

