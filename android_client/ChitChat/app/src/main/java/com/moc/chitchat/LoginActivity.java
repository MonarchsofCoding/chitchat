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

/**
 * Created by aakyo on 20/01/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    final Context loginContext = this;
    final Activity loginActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window registerWindow = getWindow();
        registerWindow.setTitle("Login");

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.login_button).getId()){
            final EditText usernameInput = (EditText) findViewById(R.id.username_input);
            final EditText passwordInput = (EditText) findViewById(R.id.password_input);
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
                    /* TODO Aydin: Lift off and check JSON format when the server is ready.
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
        else if(v.getId() == findViewById(R.id.register_button).getId()) {
            Intent registerIntent = new Intent(loginContext, RegisterUserActivity.class);
            startActivity(registerIntent);
            overridePendingTransition(R.transition.anim_enter1,R.transition.anim_enter2);
        }
    }
}
