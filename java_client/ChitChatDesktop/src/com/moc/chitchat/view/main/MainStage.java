package com.moc.chitchat.view.main;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbee.javafx.scene.layout.fxml.MigPane;

import java.awt.*;

@Component
public class MainStage extends Stage {

    private SearchPane searchPane;
    @Autowired
    public MainStage(
            SearchPane searchPane
    ) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int resX = gd.getDisplayMode().getWidth();
        int resY = gd.getDisplayMode().getHeight();

        int width = Math.round(resX/2);
        int height = Math.round(resY/2);

        this.setX((resX - width)/2);
        this.setY((resY - height)/2);


        this.setTitle("Welcome to Chit Chat");
        this.searchPane = searchPane;

        MigPane basePane = new MigPane();
        basePane.setLayout("fill");

        basePane.add(searchPane.getContentPane(), "dock west");

        this.setScene(new Scene(basePane));

    }

}
