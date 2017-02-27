package com.moc.chitchat.view;

import com.moc.chitchat.controller.authentication.LoginController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
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
    private Label unexpectedErrors;
    private AuthenticationStage stage;

    @Autowired
    LoginView(
            LoginController loginController
    ) {
        this.loginController = loginController;
    }

    void setAuthenticationStage(AuthenticationStage stage) {
        this.stage = stage;
    }

    @Override
    public MigPane getContentPane() {
        MigPane loginForm = new MigPane();

        this.usernameField = new TextField();
        this.usernameField.setId("usernameField");
        this.usernameField.setPromptText("Username");
        loginForm.add(this.usernameField,"grow,wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setId("passwordField");
        this.passwordField.setOnAction(this);
        loginForm.add(this.passwordField,"grow,wrap");


        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        this.loginBtn.setId("loginBtn");
        loginForm.add(this.loginBtn, "wrap, grow");

        this.registerBtn = new Button("Register");
        this.registerBtn.setId("registerBtn");
        this.registerBtn.setOnAction(this);
        loginForm.add(this.registerBtn, "wrap,grow");
        this.unexpectedErrors = new Label();
        this.unexpectedErrors.setId("errors");
        this.unexpectedErrors.setTextFill(Color.RED);
        this.unexpectedErrors.setVisible(false);

        loginForm.add(this.unexpectedErrors,"grow");

        MigPane loginPane = new MigPane();
        loginPane.add(loginForm, "span, split 2, center");

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
            this.stage.showMainStage();

            //  JOptionPane.showMessageDialog(frame,
            // String.format("Success! You have now registered %s!", user.getUsername()));
        } catch (ValidationException validationException) {
            Errors errors = validationException.getErrors();
            if (errors.hasErrors()) {
                if (errors.getFieldError().getField().equals(("username"))) {

                }
                if (errors.getFieldError().getField().equals("password")) {

                }
            }

            this.unexpectedErrors.setText("Wrong credentials or you have not registered yet !");
            this.unexpectedErrors.setVisible(true);



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
            this.stage.showRegister();
        }
    }
}

