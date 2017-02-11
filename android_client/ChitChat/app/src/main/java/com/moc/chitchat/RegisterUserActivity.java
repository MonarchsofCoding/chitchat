package com.moc.chitchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Result;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.baidu.unbiz.fluentvalidator.ResultCollectors.toSimple;

/**
 * Created by aakyo on 19/01/2017.
 */

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    final Context registerContext = this;
    final Activity registerUserActivity = this;
    Result userValidateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Window registerWindow = getWindow();
        registerWindow.setTitle("Register");

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            EditText usernameInput = (EditText) findViewById(R.id.username_input);
            EditText passwordInput = (EditText) findViewById(R.id.password_input);
            EditText passwordReInput = (EditText) findViewById(R.id.reinput_password_input);

            UserModel user = new UserModel(usernameInput.getText().toString(),
                passwordInput.getText().toString(),
                passwordReInput.getText().toString());

            userValidateResult = FluentValidator.checkAll().on(user, new UserInputValidator()).doValidate().result(toSimple());
            if (!userValidateResult.isSuccess()) {
                throw new Exception(userValidateResult.getErrors().toString());
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                registerContext.getResources().getString(R.string.server_url).toString(),
                user.getJSON(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Registration is successful. Response: " + response);
                        Toast.makeText(registerContext, "Registration is successful.", Toast.LENGTH_LONG).show();
                        ExitActivity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            throw new Exception(new UserResponseValidator().validateResponse(error));
                        } catch (Exception registerFail) {
                            Toast.makeText(registerContext, registerFail.getMessage().toString()
                                .replace("[","")
                                .replace("]",""), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            Volley.newRequestQueue(registerContext).add(jsonRequest);
        }
        catch (Exception registerFail) {
            Toast.makeText(registerContext, registerFail.getMessage().toString()
                .replace("[","")
                .replace("]",""), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        ExitActivity();
    }

    public void ExitActivity() {
        this.finish();
        overridePendingTransition(R.transition.anim_exit1,R.transition.anim_exit2);
    }
}
