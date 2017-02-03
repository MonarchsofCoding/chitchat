package com.moc.chitchat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aakyo on 23/01/2017.
 */


public class LoginTest {

    private String usernameTyped;
    private String passwordTyped;
    private LoginController lController;

    @Before
    public void prep_controller() {
        lController = Mockito.mock(LoginController.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void true_Input(){
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";
        lController.loginUser(usernameTyped,passwordTyped);

        String expectedOutput  = "Input Check for Login: OK.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void empty_UserInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        usernameTyped = "";
        passwordTyped = "Abc123!?";
        lController.loginUser(usernameTyped,passwordTyped);

        String expectedOutput  = "ERROR: the username cannot be empty.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }


    @Test
    public void empty_PassInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        usernameTyped = "aydinakyol";
        passwordTyped = "";
        lController.loginUser(usernameTyped,passwordTyped);

        String expectedOutput  = "ERROR: the password cannot be empty.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }


    @Test
    public void false_PassInput() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123";
        lController.loginUser(usernameTyped,passwordTyped);

        String expectedOutput  = "ERROR: the password does not match with the desired password" +
            " pattern.\n";
        assertEquals(outContent.toString(), expectedOutput);
    }
}
