package com.moc.chitchat.view;

import com.moc.chitchat.Main;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;

import java.util.concurrent.TimeoutException;

/**
 * PrimaryStageTest provides the base UI test for the application.
 */
public class PrimaryStageTest extends ApplicationTest {

    @Before
    public void setUpClass() throws Exception {
        String args = "dev";

        if (System.getenv("CHITCHAT_ENV") != null && System.getenv("CHITCHAT_ENV").length() > 0) {
            args = System.getenv("CHITCHAT_ENV");
        }
        Thread.sleep(1500);
        ApplicationTest.launch(Main.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Thread.sleep(500);
        stage.show();
    }

    @After
    public void AfterEachTest() throws TimeoutException {
    }

}
