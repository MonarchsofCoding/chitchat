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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window registerWindow = getWindow();
        registerWindow.setTitle("Login");

        final EditText usernameInput = (EditText) findViewById(R.id.username_input);
        final EditText passwordInput = (EditText) findViewById(R.id.password_input);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
