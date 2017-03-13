package com.moc.chitchat.view.authentication;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.moc.chitchat.controller.authentication.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.BaseView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * RegistrationView provides the view used for registering a User.
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

    private Label unexpectedErrors;

    @Autowired
    RegistrationView(
            RegistrationController registrationController
    ) {
        this.registrationController = registrationController;
    }

    @Override
    public MigPane getContentPane() {
        this.baseStage.setWindowTitle("Chit Chat - Registration");

        this.usernameField = new JFXTextField();
        this.usernameField.setPromptText("Username");
        this.usernameField.setId("register-username-fld");
        this.usernameField.setMinWidth(295.0); // 295.0 is the magic width
        MigPane registerForm = new MigPane();
        registerForm.setLayout("fill");
        registerForm.add(this.usernameField, "span");

        this.usernameErrors = new Label();
        this.usernameErrors.setId("register-username-errs");
        this.usernameErrors.setTextFill(Color.RED);
        this.usernameErrors.setVisible(false);
        registerForm.add(this.usernameErrors, "span");

        this.passwordField = new JFXPasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setId("register-password-fld");
        this.passwordField.setOnAction(this);
        this.passwordField.setMinWidth(295.0); // 295.0 is the magic width
        registerForm.add(this.passwordField, "span");

        this.passwordErrors = new Label();
        this.passwordErrors.setId("register-password-errs");
        this.passwordErrors.setTextFill(Color.RED);
        this.passwordErrors.setVisible(false);
        registerForm.add(this.passwordErrors, "span");

        this.passwordCheckField = new JFXPasswordField();
        this.passwordCheckField.setPromptText("Re-Password");
        this.passwordCheckField.setId("register-passwordCheck-fld");
        this.passwordCheckField.setOnAction(this);
        this.passwordCheckField.setMinWidth(295.0); // 295.0 is the magic width
        registerForm.add(this.passwordCheckField, "span");

        this.passwordCheckErrors = new Label();
        this.passwordCheckErrors.setId("register-passwordCheck-errs");
        this.passwordCheckErrors.setTextFill(Color.RED);
        this.passwordCheckErrors.setVisible(false);
        registerForm.add(this.passwordCheckErrors, "span");

        this.registerBtn = new JFXButton("Register");
        this.registerBtn.setOnAction(this);
        this.registerBtn.setId("register-register-btn");
        this.registerBtn.setMinWidth(295.0); // 295.0 is the magic width
        registerForm.add(this.registerBtn, "span");

        this.unexpectedErrors = new Label();
        this.unexpectedErrors.setId("register-errors-lbl");
        this.unexpectedErrors.setTextFill(Color.RED);
        this.unexpectedErrors.setVisible(false);
        registerForm.add(this.unexpectedErrors, "span");

        this.loginBtn = new JFXButton("Login");
        this.loginBtn.setOnAction(this);
        this.loginBtn.setId("register-login-btn");
        this.loginBtn.setMinWidth(295.0); // 295.0 is the magic width
        registerForm.add(this.loginBtn);

        MigPane registerPane = new MigPane();
        registerPane.setLayout("fill");
        registerPane.add(registerForm, "span, split 2, center");

        return registerPane;
    }

    private void enableFieldsAndButtons(boolean enabled) {
        this.usernameField.setDisable(!enabled);
        this.passwordField.setDisable(!enabled);
        this.passwordCheckField.setDisable(!enabled);
        this.registerBtn.setDisable(!enabled);
        this.loginBtn.setDisable(!enabled);
    }

    private void registerAction() {

        this.usernameErrors.setVisible(false);
        this.passwordErrors.setVisible(false);
        this.passwordCheckErrors.setVisible(false);

        this.enableFieldsAndButtons(false);

        try {
            UserModel user = this.registrationController.registerUser(
                    this.usernameField.getText(),
                    String.valueOf(this.passwordField.getText()),
                    String.valueOf(this.passwordCheckField.getText())
            );

            // Clearing the fields
            this.usernameField.clear();
            this.passwordField.clear();
            this.passwordCheckField.clear();

            this.enableFieldsAndButtons(true);

            this.baseStage.showLogin();

        } catch (ValidationException validationException) {

            this.enableFieldsAndButtons(true);

            Errors errors = validationException.getErrors();
            if (errors.hasErrors()) {
                if (errors.hasFieldErrors("username")) {
                    System.out.println("aaa" + errors.getFieldError("username").getDefaultMessage());
                    this.usernameErrors.setText(errors.getFieldError("username").getDefaultMessage());
                    this.usernameErrors.setVisible(true);
                }

                if (errors.hasFieldErrors("password")) {
                    this.passwordErrors.setText(errors.getFieldError("password").getDefaultMessage());
                    this.passwordErrors.setVisible(true);
                }

                if (errors.hasFieldErrors("passwordCheck")) {
                    this.passwordCheckErrors.setText(errors.getFieldError("passwordCheck").getDefaultMessage());
                    this.passwordCheckErrors.setVisible(true);
                }
            }

        } catch (Exception defaultError) {
            this.unexpectedErrors.setText("Unexpected error from the server");
            defaultError.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.registerAction();
        } else if (actionEvent.getSource() == this.loginBtn) {
            this.baseStage.showLogin();
        }
    }
}
