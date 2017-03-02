package com.moc.chitchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.controller.LoginController;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import com.moc.chitchat.service.ReceiveMessageService;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * LoginActivity provides the View and Actions involved with logging a User in.
 */
public class LoginActivity extends AppCompatActivity
    implements View.OnClickListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    @Inject
    LoginController loginController;
    @Inject
    ErrorResponseResolver errorResponseResolver;
    @Inject
    SessionConfiguration sessionConfiguration;

    EditText usernameField;
    EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        this.setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("ChitChat");

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        this.usernameField = (EditText) this.findViewById(R.id.username_input);
        this.passwordField = (EditText) this.findViewById(R.id.password_input);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == findViewById(R.id.login_button).getId()) {
            loginButton();
        } else if (view.getId() == findViewById(R.id.register_button).getId()) {
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
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
    }

    /**
     * onErrorResponse handles errors (validation) from the server and displays them to the User.
     *
     * @param error An error encapsulating the server response.
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error on login: Invalid credentials or you didn't registered yet");
        Toast.makeText(this,
            "Invalid credentials or you didn't registered yet",
            Toast.LENGTH_LONG
        ).show();

        sessionConfiguration.cleanCurrentUser(); //Ensuring no one logged in accidentally
    }

    /**
     * onResponse handles success responses from the server.
     *
     * @param response The response that the server sent.
     */
    @Override
    public void onResponse(JSONObject response) {
        try {
            String username = response.getJSONObject("data").get("username").toString();
            System.out.println(String.format("Successfully logged in: %s", username));
            Toast.makeText(this,
                String.format("Successfully logged in: %s", username), Toast.LENGTH_LONG).show();

            sessionConfiguration.setCurrentUser(
                sessionConfiguration.getCurrentUser()
                    .setAuthToken(response.getJSONObject("data").get("authToken").toString())
            );
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
        Intent searchIntent = new Intent(this, SearchUserActivity.class);
        startActivity(searchIntent);
        startService(new Intent(this, ReceiveMessageService.class));
        overridePendingTransition(R.transition.anim_left1, R.transition.anim_left2);
        this.exitActivity();
    }

    @Override
    public void onBackPressed() {
        exitActivity();
    }

    public void exitActivity() {
        this.finish();
        overridePendingTransition(R.transition.anim_right1, R.transition.anim_right2);
    }
}
