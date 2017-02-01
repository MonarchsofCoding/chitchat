package com.moc.chitchat;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aakyo on 23/01/2017.
 */

@Module
@Singleton
public class LoginController {

    public LoginController() {}

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
        /* TODO Aydin: Lift off the comment when the server is ready, and be sure of the JSONObject structure
        JSONObject registerObject = new JSONObject();
        JSONObject returnObject = new JSONObject();
        registerObject.put("username",usernameInput);
        registerObject.put("password",passwordInput);
        ServerComms comms = new ServerComms();
        if (comms.setRequestType("POST")) {
            returnObject = comms.requestWithJSON(registerObject);
            //TODO Aydin: Handle the response code and the message somehow (maybe a JSONObject and String tuple)
        }
        */
    }
}
