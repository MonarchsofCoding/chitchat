package com.moc.chitchat.view.authentication;

import com.moc.chitchat.controller.authentication.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * RegistrationView provides the window used for registering a User.
 */
@Component
public class RegistrationView extends BaseView implements EventHandler<ActionEvent> {

    private RegistrationController registrationController;

    private TextField usernameField;
    private Label usernameErrors;
    private PasswordField passwordField;
    private Label passwordErrors;
    private PasswordField passwordCheckField;
    private Label passwordCheckErrors;

    private Button registerBtn;
    private Button loginBtn;

    private AuthenticationStage stage;

    @Autowired
    RegistrationView(
            RegistrationController registrationController
    ) {
        this.registrationController = registrationController;
    }

    public void setStage(AuthenticationStage stage) {
        this.stage = stage;
    }

    @Override
    public MigPane getContentPane() {
        MigPane registerPane = new MigPane();

        MigPane registerForm = new MigPane();

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Username");
        registerForm.add(this.usernameField, "wrap");
        this.usernameErrors = new Label();
        registerForm.add(this.usernameErrors, "wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setOnAction(this);
        registerForm.add(this.passwordField, "span");
        this.passwordErrors = new Label();
        registerForm.add(this.passwordErrors, "wrap");

        this.passwordCheckField = new PasswordField();
        this.passwordCheckField.setPromptText("Re-Password");
        this.passwordCheckField.setOnAction(this);
        registerForm.add(this.passwordCheckField, "span");
        this.passwordCheckErrors = new Label();
        registerForm.add(this.passwordCheckErrors, "wrap");

        this.registerBtn = new Button("Register");
        this.registerBtn.setOnAction(this);
        registerForm.add(this.registerBtn, "wrap, grow");

        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        registerForm.add(this.loginBtn, "wrap, grow");

        registerPane.add(registerForm, "span, split 2, center");

        return registerPane;
    }

    private void registerAction() {
        this.usernameField.setDisable(true);
        this.passwordField.setDisable(true);
        this.passwordCheckField.setDisable(true);
        this.registerBtn.setDisable(true);
        this.loginBtn.setDisable(true);

        try {
            UserModel user = this.registrationController.registerUser(
                this.usernameField.getText(),
                String.valueOf(this.passwordField.getText()),
                String.valueOf(this.passwordCheckField.getText())
            );
//            JOptionPane.showMessageDialog(frame, String.format(
//                    "Success! You have now registered %s!",
//                    user.getUsername())
//            );
            System.out.println(String.format(
                    "Success! You have now registered %s!", user.getUsername()));

            this.loginBtn.setDisable(false);

        } catch (ValidationException validationException) {
            this.usernameField.setDisable(false);
            this.passwordField.setDisable(false);
            this.passwordCheckField.setDisable(false);
            this.registerBtn.setDisable(false);
            this.loginBtn.setDisable(false);

            Errors errors = validationException.getErrors();

            if (errors.hasFieldErrors("username")) {
                this.usernameErrors.setText(errors.getFieldError("username").getDefaultMessage());
            }

            if (errors.hasFieldErrors("password")) {
                this.passwordErrors.setText(errors.getFieldError("password").getDefaultMessage());
            }

            if (errors.hasFieldErrors("passwordCheck")) {
                this.passwordCheckErrors.setText(errors.getFieldError("passwordCheck").getDefaultMessage());
            }

        } catch (Exception defaultError) {
            defaultError.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.registerAction();
        } else if (actionEvent.getSource() == this.loginBtn) {
            this.stage.showLogin();
        }
    }
}
