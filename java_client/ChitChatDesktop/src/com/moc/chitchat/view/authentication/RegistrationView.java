package com.moc.chitchat.view.authentication;


import com.moc.chitchat.controller.authentication.RegistrationController;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * RegistrationView provides the window used for registering a User.
 */
@Component
public class RegistrationView extends JFrame implements ActionListener {

    private RegistrationController registrationController;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField passwordCheckField;

    @Autowired
    RegistrationView(RegistrationController registrationController) {
        this.registrationController = registrationController;

        this.buildInterface();
    }

    private void buildInterface() {
        this.setVisible(false);

        JPanel registerForm = new JPanel(new MigLayout());

        JLabel usernameLbl = new JLabel("Username ");
        registerForm.add(usernameLbl);
        this.usernameField = new JTextField(20);
        registerForm.add(this.usernameField, new CC().wrap());

        JLabel passwordLbl = new JLabel("Password ");
        registerForm.add(passwordLbl);
        this.passwordField = new JPasswordField(20);
        this.passwordField.addActionListener(this);
        registerForm.add(this.passwordField, new CC().wrap());

        JLabel passwordCheckLbl = new JLabel("Password again ");
        registerForm.add(passwordCheckLbl);
        this.passwordCheckField = new JPasswordField(20);
        this.passwordCheckField.setActionCommand("register");
        this.passwordCheckField.addActionListener(this);
        registerForm.add(this.passwordCheckField, new CC().wrap());

        JButton registerBtn = new JButton("Register");
        registerBtn.setActionCommand("register");
        registerBtn.addActionListener(this);
        registerForm.add(registerBtn, new CC().span(2).grow());

        this.add(registerForm);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case "register":
                this.registerAction();
        }
    }

    private void registerAction() {
        try {
            this.registrationController.registerUser(
                this.usernameField.getText(),
                String.valueOf(this.passwordField.getPassword()),
                String.valueOf(this.passwordCheckField.getPassword())
            );
        } catch (Exception e) {
            // Do validation or error handling. TODO: throw and catch more descriptive exceptions.
            System.out.println("Exception caught!");
        }
    }
}
