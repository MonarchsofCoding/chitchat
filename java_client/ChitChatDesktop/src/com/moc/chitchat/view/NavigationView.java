package com.moc.chitchat.view;


import com.moc.chitchat.view.authentication.LoginView;
import com.moc.chitchat.view.authentication.RegistrationView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

@Component
public class NavigationView implements EventHandler<ActionEvent> {

    private StackPane stackPane;

    private Button registerBtn;
    private Button loginBtn;

    private LoginView loginView;
    private RegistrationView registrationView;

    @Autowired
    public NavigationView(
        LoginView loginView,
        RegistrationView registrationView
    ) {
        this.loginView = loginView;
        this.registrationView = registrationView;
    }

    void setStackPane(StackPane stackPane) {
        this.stackPane = stackPane;

        this.stackPane.getChildren().addAll(
            this.loginView.buildContentPane()
        );
    }

    void addToPane(MigPane pane) {
        this.registerBtn = new Button("Register");
        this.registerBtn.setOnAction(this);
        pane.add(this.registerBtn, "dock east");

        this.loginBtn = new Button("Login");
        this.loginBtn.setOnAction(this);
        pane.add(this.loginBtn, "dock east");
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.registerBtn) {
            this.stackPane.getChildren().setAll(this.registrationView.buildContentPane());
        } else if (actionEvent.getSource() == this.loginBtn) {
            this.stackPane.getChildren().setAll(this.loginView.buildContentPane());
        }
    }
}
