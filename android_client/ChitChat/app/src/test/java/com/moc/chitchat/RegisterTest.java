package com.moc.chitchat;

import android.content.Context;
import android.test.mock.MockContext;

import org.json.JSONObject;
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
    private String exceptionMessage = "";
    private ServerComms mockServerComms;
    private RegisterController mockRegisterController;
    private JSONObject mockJSON;
    
    @Before
    public void prep_controller() {
        mockServerComms = Mockito.mock(ServerComms.class);
        mockRegisterController = new RegisterController(mockServerComms);
        mockJSON = Mockito.mock(JSONObject.class);
    }

    @Test
    public void true_Input(){
        usernameTyped = "aydinakyol";
        passwordTyped = "Abc123!?";
        passwordReTyped = "Abc123!?";

        try {
            mockRegisterController.registerUser(usernameTyped, passwordTyped, passwordReTyped,mockJSON);
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
            mockRegisterController.registerUser(usernameTyped, passwordTyped, passwordReTyped,mockJSON);
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
            mockRegisterController.registerUser(usernameTyped, passwordTyped, passwordReTyped,mockJSON);
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
            mockRegisterController.registerUser(usernameTyped, passwordTyped, passwordReTyped,mockJSON);
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
            mockRegisterController.registerUser(usernameTyped, passwordTyped, passwordReTyped,mockJSON);
        }
        catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals(exceptionMessage, "ERROR: the password does not match with the desired " +
            "password pattern.\n");
    }
}
