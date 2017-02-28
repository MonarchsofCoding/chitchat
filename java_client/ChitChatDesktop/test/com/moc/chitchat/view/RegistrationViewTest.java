package com.moc.chitchat.view;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by AIGERIM on 28/02/2017.
 */
public class RegistrationViewTest extends PrimaryStageTest {

    final String loginButton = "#loginBtn";
    final String usernamefield= "#usernamefieldreg";
    final String passwordfield= "#passwordFieldreg";
    final String registerBtn = "#registerBtn";
    final String registerBtnreg = "#registerBtnreg";
    final String servererrors = "#servererrorsreg";
    final String passwordErrors = "#passwordErrorsreg";
    final String passwordCheckErrors = "#passwordCheckErrors";
    final String usernameErrors = "#usernameErrorsreg";

    /**
     * Check the writing behaviour of our fields.
     */
    @Test
     public void testTextFieldsBehaviors() {

       clickOn(registerBtn).clickOn(usernamefield).write("Aika").clickOn(registerBtnreg);
       assertTrue(find(servererrors).isVisible());
       assertFalse(find(usernameErrors).isVisible());
        assertTrue(find(passwordErrors).isVisible());



   }
}

