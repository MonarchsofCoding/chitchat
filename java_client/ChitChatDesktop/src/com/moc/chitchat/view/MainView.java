package com.moc.chitchat.view;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

@Component
public class MainView {

    private NavigationView navigationView;

    @Autowired
    public MainView(
        NavigationView navigationView
    ) {
        this.navigationView = navigationView;
    }

    public void load(Stage stage) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX/2);
        int height = Math.round(resY/2);

        stage.setX((resX - width)/2);
        stage.setY((resY - height)/2);

        stage.setTitle("Chit Chat");

        MigPane basePane = new MigPane();
        basePane.setLayout("fill");
        basePane.setStyle("-fx-background-color: #fff");

        StackPane stackPane = new StackPane();
        this.navigationView.setStackPane(stackPane);

        // Attach header
        basePane.add(this.buildHeader(), "dock north");

        // Attach content
        basePane.add(stackPane, "grow");

        // Attach footer
        basePane.add(this.buildFooter(), "dock south");

        Scene scene = new Scene(basePane, width, height);
        stage.setScene(scene);
        stage.show();
        basePane.requestFocus();
    }

    private MigPane buildHeader() {
        MigPane headerPane = new MigPane();
        headerPane.setLayout("fill");
        headerPane.setStyle("-fx-background-color: #888;");
        headerPane.setMinHeight(40);

        this.navigationView.addToPane(headerPane);

        return headerPane;
    }

    private MigPane buildFooter() {
        MigPane footerPane = new MigPane();
        footerPane.setLayout("fill");
        footerPane.setStyle("-fx-background-color: #AAA;");
        footerPane.setMinHeight(30);

        javafx.scene.control.Label creditsLbl = new javafx.scene.control.Label("Created by: Monarchs of Coding @ KCL");
        creditsLbl.setStyle("-fx-alignment: center");
//        footerPane.add(creditsLbl, "grow");
        footerPane.add(creditsLbl, "dock east, gapright 5");
        return footerPane;
    }
}
