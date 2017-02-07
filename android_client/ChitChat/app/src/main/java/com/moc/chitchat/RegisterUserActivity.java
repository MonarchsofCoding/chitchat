package com.moc.chitchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by aakyo on 19/01/2017.
 */

public class RegisterUserActivity extends AppCompatActivity {

    final Context registerContext = this;
    final Activity thisActivity = this;
    private RegisterController rController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        try {
            rController = new RegisterController(new ServerComms(getResources().getString(R.string.server_url).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Window registerWindow = getWindow();
        registerWindow.setTitle("Register");

        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        final EditText passwordReInput = (EditText) findViewById(R.id.reinput_password_input);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameInput.getText().toString().equals("")) {
                    System.out.println("ERROR: the username cannot be empty.");
                    Toast.makeText(registerContext, "The username cannot be empty.",
                        Toast.LENGTH_LONG).show();
                }
                else if(passwordInput.getText().toString().equals("") ||
                    passwordReInput.getText().toString().equals("")) {
                    System.out.println("ERROR: the password cannot be empty.");
                    Toast.makeText(registerContext, "The password cannot be empty.",
                        Toast.LENGTH_LONG).show();
                }
                else if(!passwordInput.getText().toString().equals(passwordReInput.getText().toString())) {
                    System.out.println("ERROR: The two password inputs do not match!");
                    Toast.makeText(registerContext, "The two password inputs do not match!",
                        Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("GUI Input Check for Registration Request: OK.");
                    try {
                        JSONObject registerObject = new JSONObject();
                        registerObject.put("username",usernameInput.getText().toString());
                        registerObject.put("password",passwordInput.getText().toString());



                        if(rController.registerUser(usernameInput.getText().toString(),passwordInput.getText().toString(),passwordReInput.getText().toString(),registerObject)) {
                            Toast.makeText(registerContext, "The registration process is successfull.", Toast.LENGTH_LONG).show();
                            thisActivity.finish();
                            overridePendingTransition(R.transition.anim_exit1,R.transition.anim_exit2);
                        }
                        else {
                            Toast.makeText(registerContext, "The registration process is unsuccessfull.", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        Toast.makeText(registerContext, "Something went wrong on our side. Try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        ExitActivity();
    }

    public void ExitActivity() {
        thisActivity.finish();
        overridePendingTransition(R.transition.anim_exit1,R.transition.anim_exit2);
    }


}
