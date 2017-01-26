package com.moc.chitchat;

import android.content.Context;
import android.widget.Toast;

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
            System.out.print("OK.\n");
            //TODO Aydin: Create JSON object and call the function
            //TODO Aydin: IF the registration is successful, store the user somehow
        }
    }
}
