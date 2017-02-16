package com.moc.chitchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.controller.RegistrationController;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.R;
import com.moc.chitchat.resolver.ErrorResponseResolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * RegistrationActivity provides the View and Actions involved with registering a User.
 */
public class RegistrationActivity extends Activity
        implements View.OnClickListener,
        Response.Listener<JSONObject>,
        Response.ErrorListener
{

    @Inject RegistrationController registrationController;
    @Inject ErrorResponseResolver errorResponseResolver;

    EditText usernameField;
    EditText passwordField;
    EditText passwordCheckField;

    /**
     * onCreate acts as a constructor and sets up the Activity.
     * @param savedInstanceState the last instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject with Dagger
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);

        this.setContentView(R.layout.activity_register);

        Window registerWindow = this.getWindow();
        registerWindow.setTitle("Register");

        Button registerButton = (Button) this.findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);

        this.usernameField = (EditText) this.findViewById(R.id.username_input);
        this.passwordField = (EditText) this.findViewById(R.id.password_input);
        this.passwordCheckField = (EditText) this.findViewById(R.id.reinput_password_input);
    }

    /**
     * onClick calls registration controller and handles local validation exceptions.
     * @param v the view that received the click event.
     */
    @Override
    public void onClick(View v) {
        try {
            this.registrationController.registerUser(
                    this,
                    this,
                    this,
                    this.usernameField.getText().toString(),
                    this.passwordField.getText().toString(),
                    this.passwordCheckField.getText().toString()
            );
        } catch (ValidationException e) {
            // Validation implementation with Maps.
            // Could use something other than toasts?
            // Turn the fields red?? Have labels?
            // TODO: can be turned into a function with the field as a parameter.
            Map<String, List<String>> errors = e.getErrors();

            if (errors.containsKey("username")) {
                List<String> usernameErrors = errors.get("username");

                Toast.makeText(this,
                        String.format("Username: %s", usernameErrors.toString()),
                        Toast.LENGTH_LONG).show();
            }

            if (errors.containsKey("password")) {
                List<String> passwordErrors = errors.get("password");

                Toast.makeText(this,
                        String.format("Password: %s", passwordErrors.toString()),
                        Toast.LENGTH_LONG).show();
            }

            if (errors.containsKey("passwordCheck")) {
                List<String> passwordCheckErrors = errors.get("passwordCheck");

                Toast.makeText(this,
                        String.format("Password Check: %s", passwordCheckErrors.toString()),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * onErrorResponse handles errors (validation) from the server and displays them to the User.
     * @param error An error encapsulating the server response.
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Error registering");

        try {
            JSONObject response = this.errorResponseResolver.getResponseBody(error);

            JSONObject responseErrors = response.getJSONObject("errors");

            if (responseErrors.has("username")) {
                JSONArray usernameErrors = responseErrors.getJSONArray("username");
                Toast.makeText(this,
                        String.format("Username: %s", usernameErrors.toString()),
                        Toast.LENGTH_LONG).show();
            }

            if (responseErrors.has("password")) {
                JSONArray passwordErrors = responseErrors.getJSONArray("password");
                Toast.makeText(this,
                        String.format("Password: %s", passwordErrors.toString()),
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
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
                String.format("Successfully registered: %s", username), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.ExitActivity();
    }

    @Override
    public void onBackPressed() {
        ExitActivity();
    }

    public void ExitActivity() {
        this.finish();
        overridePendingTransition(R.transition.anim_left1,R.transition.anim_left2);
    }
}
