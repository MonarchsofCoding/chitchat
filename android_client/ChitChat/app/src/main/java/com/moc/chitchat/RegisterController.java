package com.moc.chitchat;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import org.json.JSONObject;

import javax.inject.Singleton;
import javax.net.ssl.HttpsURLConnection;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aakyo on 23/01/2017.
 */

@Module
public class RegisterController {

    @Provides
    @Singleton
    public RegisterController RegisterController() {
        return new RegisterController();
    }

    public void registerUser(String usernameInput, String passwordInput, String passwordReInput) throws Exception{
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

            JSONObject registerObject = new JSONObject();
            JSONObject returnObject = new JSONObject();
            registerObject.put("username",usernameInput);
            registerObject.put("password",passwordInput);
            ServerComms comms = new ServerComms(Resources.getSystem().getString(R.string.server_url));
            if (comms.setRequestType("POST")) {
                returnObject = comms.requestWithJSON(registerObject);
                //TODO Aydin: Handle the response code and the message somehow (maybe a JSONObject and String tuple)
                //TODO Everybody else: Below code is a debug print code. Just to see what is the returning JSON
                System.out.println(returnObject);
            }
        }
    }
}
