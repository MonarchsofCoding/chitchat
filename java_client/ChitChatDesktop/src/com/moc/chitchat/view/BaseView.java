package com.moc.chitchat.view;

import com.jfoenix.controls.JFXSpinner;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.tbee.javafx.scene.layout.fxml.MigPane;

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
        base.add(contentPane, "grow");

        base.add(this.buildFooter(), "dock south");

        return new Scene(base, this.width, this.height);
    }

    private MigPane buildHeader() {
        MigPane header = new MigPane();
        header.setLayout("fill");
        header.setStyle("-fx-background-color: #32292F");

        Label title = new Label("Chit Chat");
        title.setTextFill(Color.WHITE);
        header.add(title);

        return header;
    }

    private MigPane buildFooter() {
        MigPane footer = new MigPane();
        footer.setLayout("fill");
        footer.setStyle("-fx-background-color: #705D56;");

        Label credits = new Label("Created by: Monarchs of Coding");
        credits.setId("credits");
        credits.setTextFill(Color.WHITE);
        footer.add(credits);

        if (this.baseStage.getConfiguration() != null && this.baseStage.getConfiguration().getLoggedInUser() != null) {
            String username = this.baseStage.getConfiguration().getLoggedInUser().getUsername();

            Label loggedInAs = new Label(String.format("Logged in as: %s", username));
            loggedInAs.setTextFill(Color.WHITE);
            loggedInAs.setId("loggedInAs");
            footer.add(loggedInAs, "right");
        }

        return footer;
    }

}
