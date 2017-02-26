package com.moc.chitchat.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.tbee.javafx.scene.layout.fxml.MigPane;


public abstract class BaseView {

    private int width;
    private int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected abstract MigPane getContentPane();

    /**
     * getScene creates a new scene for the baseview.
     * @return - returns a new scene
     */
    public Scene getScene() {

        MigPane base = new MigPane();
        base.setLayout("fill");
        base.add(this.buildHeader(), "dock north");

        MigPane contentPane = this.getContentPane();
        contentPane.setLayout("fill");
        contentPane.setStyle("-fx-background-color: #F0F7F4");
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
        header.add(title, "span, split 2, center");

        return header;
    }

    private MigPane buildFooter() {
        MigPane footer = new MigPane();
        footer.setLayout("fill");
        footer.setStyle("-fx-background-color: #705D56;");

        Label credits = new Label("Created by: Monarchs of Coding");
        credits.setTextFill(Color.WHITE);
        footer.add(credits);

        return footer;
    }
}
