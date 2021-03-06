package com.moc.chitchat.view;

import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.view.authentication.LoginView;
import com.moc.chitchat.view.authentication.RegistrationView;
import com.moc.chitchat.view.main.ConversationListView;
import com.moc.chitchat.view.main.MainView;
import com.moc.chitchat.view.main.SearchView;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.Observable;
import java.util.Observer;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides the application stage.
 */
@Component
public class BaseStage implements Observer {

    private LoginView loginView;
    private RegistrationView registrationView;
    private MainView mainView;

    private Stage primaryStage;
    private Configuration configuration;
    private ConversationListView conversationListView;
    private SearchView searchView;

    /**
     * Construction for the BaseStage.
     *
     * @param mainView         - The main primaryStage
     * @param loginView        - The login in view
     * @param registrationView - The registration view
     */
    @Autowired
    public BaseStage(
            MainView mainView,
            LoginView loginView,
            RegistrationView registrationView,
            Configuration configuration,
            ConversationListView conversationListView,
            SearchView searchView
    ) {
        this.mainView = mainView;
        this.loginView = loginView;
        this.registrationView = registrationView;
        this.configuration = configuration;
        this.conversationListView = conversationListView;
        this.searchView = searchView;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setWindowTitle(String title) {
        this.primaryStage.setTitle(title);
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * shows the authentication scene.
     */
    public void show() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX / 2);
        int height = Math.round(resY / 2);

        this.primaryStage.setX((resX - width) / 2);
        this.primaryStage.setY((resY - height) / 2);

        this.loginView.setBaseStage(this);
        this.loginView.setWidth(width);
        loginView.setHeight(height);

        registrationView.setBaseStage(this);
        registrationView.setWidth(width);
        registrationView.setHeight(height);

        this.mainView.setBaseStage(this);
        this.mainView.setWidth(width);
        this.mainView.setHeight(height);

        this.showLogin();
        this.primaryStage.show();
        this.configuration.addObserver(this);
    }


    public void showLogin() {
        this.primaryStage.setScene(this.loginView.getScene());
    }

    public void showRegister() {
        this.primaryStage.setScene(this.registrationView.getScene());
    }

    public void showMainView() {
        this.primaryStage.setScene(this.mainView.getScene());
    }

    /**
     * Clear the errormessages and the listobjects.
     */
    public void clearListView() {
        this.conversationListView.clearConversationListView();
        this.configuration.logout();
        this.searchView.clearfields();
    }

    @Override
    public void update(Observable observable, Object obj) {
        this.showLogin();
    }
}
