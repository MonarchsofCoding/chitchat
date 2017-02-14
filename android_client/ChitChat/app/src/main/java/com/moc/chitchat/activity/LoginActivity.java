package com.moc.chitchat.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.controller.LoginController;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * LoginActivity provides the View and Actions involved with logging a User in.
 */
public class LoginActivity extends Activity
    implements View.OnClickListener,
        Response.Listener<JSONObject>,
        Response.ErrorListener
{

    @Inject LoginController loginController;
    @Inject ErrorResponseResolver errorResponseResolver;

    EditText usernameField;
    EditText passwordField;

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

        this.usernameField = (EditText) this.findViewById(R.id.username_input);
        this.passwordField = (EditText) this.findViewById(R.id.password_input);
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
        this.loginController.loginUser(
            this,
            this,
            this,
            this.usernameField.getText().toString(),
            this.passwordField.getText().toString()
        );
    }

    private void registerButton() {
        Intent registerIntent = new Intent(this, RegistrationActivity.class);
        startActivity(registerIntent);
    }

    /**
     * onErrorResponse handles errors (validation) from the server and displays them to the User.
     * @param error An error encapsulating the server response.
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error registering");

        try {

            Toast.makeText(this,
                String.format("Invalid credentials or you didn't registered yet"),
                Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onResponse handles success responses from the server.
     * @param response The response that the server sent.
     */
    @Override
    public void onResponse(JSONObject response) {
        System.out.println(response.toString());

        try {
            String username = response.getJSONObject("data").get("username").toString();
            Toast.makeText(this,
                String.format("Successfully logged in: %s", username), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.finish();
    }
}
