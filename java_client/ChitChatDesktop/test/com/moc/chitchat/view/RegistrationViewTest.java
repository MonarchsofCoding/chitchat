package com.moc.chitchat.view;

import javafx.scene.Parent;
import javafx.scene.layout.Region;
import org.junit.Test;

/**
 * Created by spiros on 28/02/2017.
 */
public class RegistrationViewTest extends PrimaryStageTest {
    final String loginButton = "#loginBtn";
    final String usernamefield= "#usernameField";
    final String passwordfield= "#passwordFieldreg";
    final String registerBtn = "#registerBtn";
    final String errorslabel = "#errors";

    @Test
    public void testTextFieldsBehaviors() {
        String username = "antreas";
        String password = "lalalala";
        clickOn(registerBtn).clickOn(passwordfield).write("koukos");



    }
}
