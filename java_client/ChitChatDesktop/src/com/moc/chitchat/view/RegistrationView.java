package com.moc.chitchat.view;

import com.moc.chitchat.controller.authentication.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
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
    private Label servererrors;
    private Button registerBtn;
    private Button loginBtn;

    private AuthenticationStage stage;

    @Autowired
    RegistrationView(
            RegistrationController registrationController
    ) {
        this.registrationController = registrationController;
    }

    void setAuthenticationStage(AuthenticationStage stage) {
        this.stage = stage;
    }

    @Override
    public MigPane getContentPane() {

        this.usernameField = new TextField();
        this.usernameField.setPromptText("Username");
        this.usernameField.setId("usernamefieldreg");
        MigPane registerForm = new MigPane();
        registerForm.add(this.usernameField);

        this.usernameErrors = new Label();
        this.usernameErrors.setId("usernameErrorsreg");
        this.usernameErrors.setTextFill(Color.RED);
        this.usernameErrors.setVisible(false);
        registerForm.add(this.usernameErrors, "wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setId("passwordFieldreg");
        this.passwordField.setOnAction(this);
        registerForm.add(this.passwordField);

        this.passwordErrors = new Label();
        this.passwordErrors.setId("passwordErrrorsreg");
        this.passwordErrors.setVisible(false);
        this.passwordErrors.setTextFill(Color.RED);
        registerForm.add(this.passwordErrors, "wrap");

        this.passwordCheckField = new PasswordField();
        this.passwordCheckField.setPromptText("Re-Password");
        this.passwordCheckField.setId("passwordCheckField");
        this.passwordCheckField.setOnAction(this);
        registerForm.add(this.passwordCheckField);

        this.passwordCheckErrors = new Label();
        this.passwordCheckErrors.setId("passwordCheckErrors");
        this.passwordCheckErrors.setVisible(false);
        this.passwordCheckErrors.setTextFill(Color.RED);
        registerForm.add(this.passwordCheckErrors, "wrap");

        this.registerBtn = new Button("Register");
        this.registerBtn.setOnAction(this);
        registerForm.add(this.registerBtn, "wrap, grow");

        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        registerForm.add(this.loginBtn, "wrap, grow");

        this.servererrors = new Label();
        this.servererrors.setId("servererrorsreg");
        this.servererrors.setVisible(false);
        this.servererrors.setTextFill(Color.RED);
        registerForm.add(this.servererrors,"grow");

        MigPane registerPane = new MigPane();
        registerPane.add(registerForm, "span, split 2, center");

        return registerPane;
    }

    private void changeButtonsStatus(boolean enabled) {
        if (enabled) {
            // Make the fields enabled
            this.usernameField.setDisable(false);
            this.passwordField.setDisable(false);
            this.passwordCheckField.setDisable(false);
            this.registerBtn.setDisable(false);
            this.loginBtn.setDisable(false);
        } else {
            this.usernameField.setDisable(true);
            this.passwordField.setDisable(true);
            this.passwordCheckField.setDisable(true);
            this.registerBtn.setDisable(true);
            this.loginBtn.setDisable(true);
        }
    }

    private void registerAction() {
        changeButtonsStatus(false);

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

            changeButtonsStatus(true);

            this.stage.showLogin();

        } catch (ValidationException validationException) {
            this.usernameField.setDisable(false);
            this.passwordField.setDisable(false);
            this.passwordCheckField.setDisable(false);
            this.registerBtn.setDisable(false);
            this.loginBtn.setDisable(false);

            this.servererrors.setText("Unsuccesfull Registration ");
            this.servererrors.setVisible(true);
            Errors errors = validationException.getErrors();
            if (errors.hasErrors()) {
                if (errors.hasFieldErrors("username")) {
                    this.usernameErrors.setText(errors.getFieldError("username").getDefaultMessage());
                    this.usernameErrors.setVisible(true);
                    //this.servererrors.setText();
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
            this.servererrors.setText("Username "+errors.getFieldError("username").getDefaultMessage());
            this.servererrors.setVisible(true);

        } catch (Exception defaultError) {
            this.servererrors.setText("Unexpected error from the server");
            this.servererrors.setVisible(true);
            defaultError.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.registerAction();
            if (!this.usernameField.getText().equals("")) {
                this.usernameErrors.setVisible(false);
                this.servererrors.setVisible(true);
            }
            if (!this.passwordField.getText().equals("")) {
                this.passwordErrors.setVisible(false);
                this.servererrors.setVisible(true);
            }
            if (!this.passwordCheckField.getText().equals("")) {
                this.passwordCheckErrors.setVisible(false);
                this.servererrors.setVisible(true);
            }
        } else if (actionEvent.getSource() == this.loginBtn) {
            this.stage.showLogin();
        }
    }
}
