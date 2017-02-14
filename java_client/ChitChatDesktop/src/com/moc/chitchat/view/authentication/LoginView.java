package com.moc.chitchat.view.authentication;

import com.moc.chitchat.controller.authentication.LoginController;
import com.moc.chitchat.controller.authentication.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by spiros on 09/02/17.
 */
@Component
public class LoginView extends JFrame implements ActionListener {

    private LoginController loginController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private RegistrationView registrationView;

    @Autowired
    LoginView(LoginController loginController, RegistrationView registrationView) {
        this.loginController = loginController;
        this.registrationView = registrationView;
        this.buildInterface();
    }

    private void buildInterface() {
        this.setVisible(false);

        JPanel loginForm = new JPanel(new MigLayout());

        JLabel usernameLbl = new JLabel("Username ");
        loginForm.add(usernameLbl);
        this.usernameField = new JTextField(20);
        loginForm.add(this.usernameField, new CC().wrap());

        JLabel passwordLbl = new JLabel("Password ");
        loginForm.add(passwordLbl);
        this.passwordField = new JPasswordField(20);
        this.passwordField.addActionListener(this);
        loginForm.add(this.passwordField, new CC().wrap());

        JButton loginBtn = new JButton("Login");
        loginBtn.setActionCommand("login");
        loginBtn.addActionListener(this);
        loginForm.add(loginBtn, new CC().grow());


        JButton registerBtn = new JButton("Register");
        registerBtn.setActionCommand("register");
        registerBtn.addActionListener(this);
        loginForm.add(registerBtn, new CC().grow());

        this.add(loginForm);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "login":
                this.loginAction();
                break;

            case "register":
                this.registerAction();
                break;

        }
    }

    private void registerAction() {
        registrationView.setVisible(true);
    }

    private void loginAction() {
        JFrame frame = new JFrame();
        try {
            UserModel user = this.loginController.loginUser(
                    this.usernameField.getText(),
                    String.valueOf(this.passwordField.getPassword())

            );
           JOptionPane.showMessageDialog(frame, String.format("Success! You have now login with %s!", user.getUsername()));
        } catch (ValidationException e) {
                JOptionPane.showMessageDialog(frame, "Wrong Credentials or you have not sign up yet");


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

