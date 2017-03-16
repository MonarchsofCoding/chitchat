package com.moc.chitchat.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import javax.swing.*;

/**
 * Provides the base for all primary views in the application.
 */
public abstract class BaseView {

    protected BaseStage baseStage;

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
        scene.getStylesheets().add(this.getClass().getResource("/com/moc/chitchat/resources/chitchat.css").toExternalForm());

        return scene;
    }

    private MigPane buildHeader() {
        MigPane header = new MigPane();
        header.setLayout("fill");
        header.setStyle("-fx-background-color:  #3C4F76;");
        header.setId("base-header");
        Label title = new Label("Chit Chat");
        title.setId("base-header-title");
        header.add(title, "center");
        return header;
    }

    private MigPane buildFooter() {
        MigPane footer = new MigPane();
        footer.setId("base-footer");
        footer.setLayout("fill");

        footer.setStyle("-fx-background-color:  #3C4F76;");

        Label credits = new Label("Created by: Monarchs of Coding");
        credits.setTextFill(Color.WHITE);
        credits.setId("credits");
        footer.add(credits,"left");
        if (this.baseStage.getConfiguration() != null && this.baseStage.getConfiguration().getLoggedInUser() != null) {
            String username = this.baseStage.getConfiguration().getLoggedInUser().getUsername();

            Label loggedInAs = new Label(String.format("You are: %s", username));
            loggedInAs.setId("base-footer-username");
            loggedInAs.setTextFill(Color.WHITE);
            footer.add(loggedInAs, "right");

        }

       // credits = new Label("Monarchs of Coding");
        //credits.setId("base-footer-credits");
        //footer.add(credits, "right");

        return footer;
    }

}
