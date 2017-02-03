package com.moc.chitchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by aakyo on 20/01/2017.
 */

public class LoginActivity extends AppCompatActivity {

    final Context loginContext = this;
    final Activity thisActivity = this;
    private LoginController lController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window registerWindow = getWindow();
        registerWindow.setTitle("Login");

        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);

        try {
            lController = new LoginController(new ServerComms(getResources().getString(R.string.server_url).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passRegXPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])" +
                    "(?=\\S+$).{8,}$";
                if(usernameInput.getText().toString().equals("")) {
                    System.out.println("ERROR: the username cannot be empty.");
                }
                else if(passwordInput.getText().toString().equals("")){
                    System.out.println("ERROR: the password cannot be empty.");
                }
                else if (!passwordInput.getText().toString().matches(passRegXPattern)) {
                    System.out.println("ERROR: the password does not match with the desired password" +
                        " pattern.");
                }
                else {
                    System.out.println("OK.");
                }
                //TODO Aydin: HTTPS-JSON AsyncTask execute.
            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(loginContext, RegisterUserActivity.class);
                startActivity(registerIntent);
                overridePendingTransition(R.transition.anim_enter1,R.transition.anim_enter2);
            }
        });

    }
}
