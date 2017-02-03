package com.moc.chitchat;


import org.json.JSONObject;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aakyo on 23/01/2017.
 */

@Module
@Singleton
public class LoginController {

    private String URLtoPass;

    public LoginController(String URL) {
        URLtoPass = URL;
    }

    public void loginUser(String username, String password) {
        String passRegXPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])" +
            "(?=\\S+$).{8,}$";
        if(username.equals("")) {
            System.out.print("ERROR: the username cannot be empty.\n");
        }
        else if(password.equals("")){
            System.out.print("ERROR: the password cannot be empty.\n");
        }
        else if (!password.matches(passRegXPattern)) {
            System.out.print("ERROR: the password does not match with the desired password" +
                " pattern.\n");
        }
        else {
            System.out.print("Input Check for Login: OK.\n");
        }
        /*
        try {
            JSONObject registerObject = new JSONObject();
            registerObject.put("username", username);
            registerObject.put("password", password);
            ServerComms comms = new ServerComms(URLtoPass);
            if (comms.setRequestType("POST")) {
                comms.requestWithJSON(registerObject);
            }
        }
        catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        */
    }
}
