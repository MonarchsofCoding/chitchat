package com.moc.chitchat;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.inject.Inject;

import dagger.Component;

import static junit.framework.Assert.assertEquals;

/**
 * Created by aakyo on 23/01/2017.
 */

public class RegisterTest {

    private String usernameTyped;
    private String passwordTyped;
    private String passwordReTyped;
    String exceptionMessage = "";
    @Inject RegisterController rController;
    @Mock Context mockedRegisterContext;
    
    @Before
    public void prep_controller() {
        rController = Mockito.mock(RegisterController.class, Mockito.CALLS_REAL_METHODS);
        mockedRegisterContext = new MockContext();
    }

    @Test
    public void true_Input(){
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Abc123!?";

        try {
            rController.registerUser(usernameTyped, passwordTyped, passwordReTyped);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("", exceptionMessage);
    }

    @Test
    public void empty_UserInput() {
        usernameTyped = "";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Abc123!?";
        try {
            rController.registerUser(usernameTyped, passwordTyped, passwordReTyped);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals(exceptionMessage, "ERROR: the username cannot be empty.\n");
    }


    @Test
    public void empty_PassInput() {
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";
        passwordReTyped = "";
        try {
            rController.registerUser(usernameTyped, passwordTyped, passwordReTyped);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals(exceptionMessage, "ERROR: the password cannot be empty.\n");
    }

    @Test
    public void false_PassMatch() {
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Def123!?";
        try {
            rController.registerUser(usernameTyped, passwordTyped, passwordReTyped);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals(exceptionMessage, "ERROR: The two password inputs do not match!\n");
    }


    @Test
    public void false_PassInput() {
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123";
        passwordReTyped = "Abc123";
        try {
            rController.registerUser(usernameTyped, passwordTyped, passwordReTyped);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals(exceptionMessage, "ERROR: the password does not match with the desired " +
            "password pattern.\n");
    }
}
