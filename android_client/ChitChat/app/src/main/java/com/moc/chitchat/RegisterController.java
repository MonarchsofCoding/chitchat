package com.moc.chitchat;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.widget.Toast;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aakyo on 23/01/2017.
 */

@Module
public class RegisterController {

    private ServerComms comms;

    public RegisterController(ServerComms newComm) {
        comms = newComm;
    }

    public Boolean registerUser(String usernameInput, String passwordInput, String passwordReInput, JSONObject registerObject) throws Exception{
        Boolean toReturn = false;
        String passRegXPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])" +
            "(?=\\S+$).{8,}$";
        if(usernameInput.equals("")) {
            throw new MessageException("ERROR: the username cannot be empty.\n");
        }
        else if(passwordInput.equals("") || passwordReInput.equals("")) {
            throw new MessageException("ERROR: the password cannot be empty.\n");
        }
        else if (!passwordInput.matches(passRegXPattern)) {
            throw new MessageException("ERROR: the password does not match with the desired " +
                "password pattern.\n");
        }
        else if(!passwordInput.equals(passwordReInput)) {
            throw new MessageException("ERROR: The two password inputs do not match!\n");
        }
        else {
            System.out.print("Input Check for Registration: OK.\n");
            if (comms.setRequestType("POST")) {
                try {
                    System.out.println("Sending JSON request to Servr Communication controller.");
                    comms.requestWithJSON(registerObject);
                    toReturn = true;
                }
                catch (Exception e) {
                    System.out.println(e.getStackTrace());
                }
            }

        }
        return toReturn;
    }
}
