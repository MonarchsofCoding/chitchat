package com.moc.chitchat;

import android.content.Context;
import android.widget.ProgressBar;

import org.json.JSONObject;
/**
 * Created by aakyo on 23/01/2017.
 */

public class RegisterController {

    private ServerComms comms;

    public RegisterController(ServerComms newComm) {
        comms = newComm;
    }

    public Boolean registerUser(String usernameInput, String passwordInput, String passwordReInput, JSONObject registerObject) throws Exception{
        Boolean toReturn = false;
        if(usernameInput.equals("")) {
            throw new MessageException("ERROR: the username cannot be empty.\n");
        }
        else if(passwordInput.equals("") || passwordReInput.equals("")) {
            throw new MessageException("ERROR: the password cannot be empty.\n");
        }
        else if(!passwordInput.equals(passwordReInput)) {
            throw new MessageException("ERROR: The two password inputs do not match!\n");
        }
        else {
            System.out.print("Input Check for Registration: OK.\n");
            try {
                System.out.println("Sending JSON request to Servr Communication controller.");
                int returnCode = comms.requestWithJSON(registerObject,"POST");
                if (returnCode == 201) {
                    toReturn = true;
                }
            }
            catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        return toReturn;
    }
}
