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
public class RegistrationView implements EventHandler<ActionEvent> {

    private RegistrationController registrationController;

    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField passwordCheckField;

    private Button registerBtn;

    @Autowired
    RegistrationView(
            RegistrationController registrationController
    ) {
        this.registrationController = registrationController;
    }

    public MigPane buildContentPane() {
        MigPane registerPane = new MigPane();
        registerPane.setLayout("fill");

        Label usernameLbl = new Label("Username ");
        registerPane.add(usernameLbl, "span 2");
        this.usernameField = new TextField();
        this.usernameField.setStyle("-fx-alignment: center;");
        registerPane.add(this.usernameField, "wrap");

        Label passwordLbl = new Label("Password ");
        registerPane.add(passwordLbl, "span 2");
        this.passwordField = new PasswordField();
        this.passwordField.setOnAction(this);
        registerPane.add(this.passwordField, "span");

        Label passwordCheckLbl = new Label("Password ");
        registerPane.add(passwordCheckLbl, "span 2");
        this.passwordCheckField = new PasswordField();
        this.passwordCheckField.setOnAction(this);
        registerPane.add(this.passwordCheckField, "span");

        this.registerBtn = new Button("Register");
        this.registerBtn.setOnAction(this);
        registerPane.add(registerBtn, "");

        return registerPane;
    }

    private void registerAction() {
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
        } catch (ValidationException validationException) {
            Errors errors = validationException.getErrors();

//            if (errors.getFieldError().getField().equals(("username"))) {
//                JOptionPane.showMessageDialog(frame, "Username "
//                        + errors.getFieldError("username").getDefaultMessage());
//            }
//
//            if (errors.getFieldError().getField().equals("password")) {
//                JOptionPane.showMessageDialog(frame, "Password "
//                        + errors.getFieldError("password").getDefaultMessage());
//            }
//
//            if (errors.getFieldError().getField().equals("passwordCheck")) {
//                JOptionPane.showMessageDialog(frame, "Password again "
//                        + errors.getFieldError("passwordCheck").getDefaultMessage());
//            }
        } catch (Exception defaultError) {
            defaultError.printStackTrace();
        }
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.registerAction();
        }
    }
}
