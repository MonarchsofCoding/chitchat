package com.moc.chitchat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by aakyo on 19/01/2017.
 */

public class RegisterUserActivity extends AppCompatActivity {

    final Context registerContext = this;
    final Activity thisActivity = this;
    RegisterController rController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rController = new RegisterController(getResources().getString(R.string.server_url).toString());

        Window registerWindow = getWindow();
        registerWindow.setTitle("Register");

        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);
        final EditText passwordReInput = (EditText) findViewById(R.id.reinput_password_input);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passRegXPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])" +
                    "(?=\\S+$).{8,}$";
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
                else if (!passwordInput.getText().toString().matches(passRegXPattern)) {
                    System.out.println("ERROR: the password does not match with the desired " +
                        "password pattern.");
                    Toast.makeText(registerContext, "The password must have at least one " +
                        "digit, at least one lowercase, at least one upper case, at least one " +
                        "special character, no whitespaces, and must be at least 8 characters " +
                        "long.", Toast.LENGTH_LONG).show();
                }

                else if(!passwordInput.getText().toString().equals(passwordReInput.getText().toString())) {
                    System.out.println("ERROR: The two password inputs do not match!");
                    Toast.makeText(registerContext, "The two password inputs do not match!",
                        Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("GUI Input Check for Registration Request: OK.");
                    try {
                        if(rController.registerUser(usernameInput.getText().toString(),passwordInput.getText().toString(),passwordReInput.getText().toString())) {
                            Toast.makeText(registerContext, "The registration process is successfull.", Toast.LENGTH_LONG).show();
                            thisActivity.finish();
                            overridePendingTransition(R.transition.anim_exit1,R.transition.anim_exit2);
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
