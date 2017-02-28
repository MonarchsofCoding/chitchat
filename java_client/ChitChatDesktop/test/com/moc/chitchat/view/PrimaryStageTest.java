package com.moc.chitchat.view;

import com.moc.chitchat.Main;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

/**
 * Created by spiros on 26/02/2017.
 */
public class PrimaryStageTest extends ApplicationTest {

    @Before
    public void setUpClass() throws Exception{
        ApplicationTest.launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }

    @After
    public void AfterEachTest() throws TimeoutException{
//        FxToolkit.registerPrimaryStage();
      //  FxToolkit.setupApplication(Main.class);
//
// FxToolkit.hideStage();
//        release(new KeyCode[]{});
//        release(new MouseButton[]{});
    }
    public <T extends Node> T find (final String query){
        return (T) lookup(query).queryAll().iterator().next();
    }

}
