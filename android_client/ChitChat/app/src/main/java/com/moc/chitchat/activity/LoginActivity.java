package com.moc.chitchat.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;

import org.json.JSONObject;

/**
 * LoginActivity provides the View and Actions involved with logging a User in.
 */
public class LoginActivity extends Activity
    implements View.OnClickListener,
        Response.Listener<JSONObject>,
        Response.ErrorListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        this.setContentView(R.layout.activity_login);
        this.getWindow().setTitle("Login");

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == findViewById(R.id.login_button).getId()) {
            loginButton();
        } else if (v.getId() == findViewById(R.id.register_button).getId()) {
            registerButton();
        }
    }

    private void loginButton() {
    }

    private void registerButton() {
        Intent registerIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
