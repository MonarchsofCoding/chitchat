package com.moc.chitchat.view.authentication;

import com.moc.chitchat.controller.authentication.RegistrationController;
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

import static javafx.scene.input.KeyCode.T;

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
        registerForm.add(this.usernameField);
        this.usernameErrors = new Label();
        this.usernameErrors.setTextFill(Color.RED);
        registerForm.add(this.usernameErrors, "wrap");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.setOnAction(this);
        registerForm.add(this.passwordField);
        this.passwordErrors = new Label();
        this.passwordErrors.setTextFill(Color.RED);
        registerForm.add(this.passwordErrors, "wrap");

        this.passwordCheckField = new PasswordField();
        this.passwordCheckField.setPromptText("Re-Password");
        this.passwordCheckField.setOnAction(this);
        registerForm.add(this.passwordCheckField);
        this.passwordCheckErrors = new Label();
        this.passwordCheckErrors.setTextFill(Color.RED);
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Success Registration");
            alert.setTitle("Information of Registration");
            alert.setContentText("You hane now registered as "+user.getUsername());
            alert.show();

            this.loginBtn.setDisable(false);

        } catch (ValidationException validationException) {
            this.usernameField.setDisable(false);
            this.passwordField.setDisable(false);
            this.passwordCheckField.setDisable(false);
            this.registerBtn.setDisable(false);
            this.loginBtn.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Unsuccesfull Registration ");
            alert.show();

            Errors errors = validationException.getErrors();
            if(errors.hasErrors()){
            if (errors.hasFieldErrors("username")) {
                this.usernameErrors.setText(errors.getFieldError("username").getDefaultMessage());
                alert.setContentText("Username "+errors.getFieldError("username").getDefaultMessage());
                alert.show();
            }

            if (errors.hasFieldErrors("password")) {
                this.passwordErrors.setText(errors.getFieldError("password").getDefaultMessage());
                alert.setContentText("Password "+errors.getFieldError("password").getDefaultMessage());
                alert.show();
            }

            if (errors.hasFieldErrors("passwordCheck")) {
                this.passwordCheckErrors.setText(errors.getFieldError("passwordCheck").getDefaultMessage());
                alert.setContentText("Password "+errors.getFieldError("passwordCheck").getDefaultMessage());
                alert.show();
            }
            }

        } catch (Exception defaultError) {
            defaultError.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.registerAction();
            if(!this.usernameField.getText().equals("")) this.usernameErrors.setText("");
            if(!this.passwordField.getText().equals("")) this.passwordErrors.setText("");
            if(!this.passwordCheckField.getText().equals(""))this.passwordCheckErrors.setText("");
        } else if (actionEvent.getSource() == this.loginBtn) {
            this.stage.showLogin();
        }
    }
}
