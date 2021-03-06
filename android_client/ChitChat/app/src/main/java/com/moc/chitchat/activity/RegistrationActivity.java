package com.moc.chitchat.activity;

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
import com.moc.chitchat.controller.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * RegistrationActivity provides the View and Actions involved with registering a User.
 */
public class RegistrationActivity extends AppCompatActivity
    implements View.OnClickListener,
    Response.Listener<JSONObject>,
    Response.ErrorListener {

    @Inject
    RegistrationController registrationController;
    @Inject
    ErrorResponseResolver errorResponseResolver;
    @Inject
    SessionConfiguration sessionConfiguration;

    EditText usernameField;
    EditText passwordField;
    EditText passwordCheckField;
    Button registerButton;

    /**
     * onCreate acts as a constructor and sets up the Activity.
     *
     * @param savedInstanceState the last instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        sessionConfiguration.setCurrentActivity(this);

        this.setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        registerButton = (Button) this.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        this.usernameField = (EditText) this.findViewById(R.id.username_input);
        this.passwordField = (EditText) this.findViewById(R.id.password_input);
        this.passwordCheckField = (EditText) this.findViewById(R.id.reinput_password_input);
    }

    /**
     * onClick calls registration controller and handles local validation exceptions.
     * the view that received the click event.
     */
    @Override
    public void onClick(View view) {
        try {
            registerButton.setEnabled(false);
            this.registrationController.registerUser(
                this,
                this,
                this,
                this.usernameField.getText().toString(),
                this.passwordField.getText().toString(),
                this.passwordCheckField.getText().toString()
            );
        } catch (ValidationException excevalid) {
            registerButton.setEnabled(true);
            Map<String, List<String>> errors = excevalid.getErrors();

            if (errors.containsKey("username")) {
                List<String> usernameErrors = errors.get("username");

                Toast.makeText(this,
                    String.format("Username: %s", usernameErrors.toString()),
                    Toast.LENGTH_LONG).show();
                System.out.println(String.format("Username: %s", usernameErrors.toString()));
            }

            if (errors.containsKey("password")) {
                List<String> passwordErrors = errors.get("password");

                Toast.makeText(this,
                    String.format("Password: %s", passwordErrors.toString()),
                    Toast.LENGTH_LONG).show();
                System.out.println(String.format("Password: %s", passwordErrors.toString()));
            }

            if (errors.containsKey("passwordCheck")) {
                List<String> passwordCheckErrors = errors.get("passwordCheck");

                Toast.makeText(this,
                    String.format("Password Check: %s", passwordCheckErrors.toString()),
                    Toast.LENGTH_LONG).show();
                System.out.println(String.format("Password Check: %s", passwordCheckErrors.toString()));
            }
        }
    }

    /**
     * onErrorResponse handles errors (validation) from the server and displays them to the User.
     *
     * @param error An error encapsulating the server response.
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        registerButton.setEnabled(true);
        try {
            JSONObject response = this.errorResponseResolver.getResponseBody(error);

            JSONObject responseErrors = response.getJSONObject("errors");

            if (responseErrors.has("username")) {
                JSONArray usernameErrors = responseErrors.getJSONArray("username");
                Toast.makeText(this,
                    String.format("Username: %s", usernameErrors.toString()),
                    Toast.LENGTH_LONG).show();
                System.out.println(String.format("Username: %s", usernameErrors.toString()));
            }

            if (responseErrors.has("password")) {
                JSONArray passwordErrors = responseErrors.getJSONArray("password");
                Toast.makeText(this,
                    String.format("Password: %s", passwordErrors.toString()),
                    Toast.LENGTH_LONG).show();
                System.out.println(String.format("Password: %s", passwordErrors.toString()));
            }
        } catch (JSONException jsonexcep) {
            jsonexcep.printStackTrace();
        }
    }

    /**
     * onResponse handles success responses from the server.
     *
     * @param response The response that the server sent.
     */
    @Override
    public void onResponse(JSONObject response) {
        registerButton.setEnabled(true);
        try {
            String username = response.getJSONObject("data").get("username").toString();
            Toast.makeText(this,
                String.format("Successfully registered: %s", username), Toast.LENGTH_LONG).show();
            System.out.println(String.format("Successfully registered: %s", username));
        } catch (JSONException jsonexception) {
            jsonexception.printStackTrace();
        }
        this.exitActivity();
    }

    @Override
    public void onBackPressed() {
        exitActivity();
    }

    public void exitActivity() {
        this.finish();
        overridePendingTransition(R.transition.anim_left1, R.transition.anim_left2);
    }
}
