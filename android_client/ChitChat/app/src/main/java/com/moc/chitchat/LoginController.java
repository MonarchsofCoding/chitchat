package com.moc.chitchat;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aakyo on 23/01/2017.
 */

@Module
public class LoginController {

    @Provides
    @Singleton
    public LoginController LoginController() {
        return new LoginController();
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
            System.out.print("OK.\n");
        }
        //TODO Aydin: HTTPS-JSON AsyncTask execute.
    }
}
