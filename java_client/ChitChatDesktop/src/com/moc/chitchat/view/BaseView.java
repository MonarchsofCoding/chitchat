package com.moc.chitchat.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.tbee.javafx.scene.layout.fxml.MigPane;

/**
 * Provides the base for all primary views in the application.
 */
public abstract class BaseView implements EventHandler<ActionEvent> {

    protected BaseStage baseStage;
    private Button logout;
    private int width;
    private int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setBaseStage(BaseStage stage) {
        this.baseStage = stage;
    }

    protected abstract MigPane getContentPane();

    /**
     * getScene creates a new scene for the baseview.
     *
     * @return - returns a new scene
     */
    public Scene getScene() {

        MigPane base = new MigPane();
        base.setLayout("fill, wrap 12");
        base.add(this.buildHeader(), "dock north");

        MigPane contentPane = this.getContentPane();
        contentPane.setId("base-content-pane");
        base.add(contentPane, "grow");

        base.add(this.buildFooter(), "dock south");

        Scene scene = new Scene(base, this.width, this.height);
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Lato");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Fredericka+the+Great");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Playfair+Display:700");
        scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Pacifico");
        scene.getStylesheets().add(this.getClass()
                .getResource("/com/moc/chitchat/resources/chitchat.css")
                .toExternalForm());

        return scene;
    }

    private MigPane buildHeader() {
        MigPane header = new MigPane();
        header.setLayout("fill");
        header.getStyleClass().add("base-header");
        header.setId("base-header");
        Label title = new Label("Chit Chat");
        title.setWrapText(true);
        title.setId("base-header-title");
        if (this.baseStage.getConfiguration() != null
                && this.baseStage.getConfiguration().getLoggedInUser() != null) {

            logout = new Button("Log out");
            logout.setOnAction(this);
            logout.getStyleClass().add("logout-button");
            logout.setId("logout-button");
            header.add(title, "center,wrap");
            header.add(logout, "center");
        } else {
            header.add(title, "center, wrap");
        }

        return header;
    }

    private MigPane buildFooter() {
        MigPane footer = new MigPane();
        footer.setId("base-footer");
        footer.setLayout("fill");
        footer.getStyleClass().add("base-footer");
        Label credits = new Label("Created by: Monarchs of Coding");
        credits.setTextFill(Color.WHITE);
        credits.setId("credits");
        footer.add(credits, "left");
        if (this.baseStage.getConfiguration() != null && this.baseStage.getConfiguration().getLoggedInUser() != null) {
            String username = this.baseStage.getConfiguration().getLoggedInUser().getUsername();

            Label loggedInAs = new Label(String.format("You are: %s", username));
            loggedInAs.setId("base-footer-username");
            loggedInAs.setTextFill(Color.WHITE);
            footer.add(loggedInAs, "right");

        }

        return footer;
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == this.logout) {
            this.baseStage.getConfiguration().setLoggedInUser(null);
            baseStage.showLogin();
        }
    }

}

