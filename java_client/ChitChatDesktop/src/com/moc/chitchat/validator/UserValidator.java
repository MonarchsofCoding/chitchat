package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModel user = (UserModel) target;

        if (user.getUsername().isEmpty()) {
            errors.rejectValue("username", "field.required", "cannot be empty");
        }

        String pattern = "^[a-zA-Z0-9_]*$";
        if(!user.getUsername().matches(pattern)) {
            errors.rejectValue("username", "field.required", "invalid username");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "field.required", "cannot be empty");
        }

        if (user.getPasswordCheck() == null ||!user.getPasswordCheck().equals(user.getPassword())) {
            errors.rejectValue("passwordCheck", "password.mismatch", "should match password");
        }
    }

    /**
     * Validating the user of it's username and password to sign up.
     *
     * @param user - validate the UserModel user
     * @throws ValidationException - if username or password is incorrect
     */
    public void validate(UserModel user) throws ValidationException {
        MapBindingResult errors = new MapBindingResult(new HashMap<String, String>(), UserModel.class.getName());
        this.validate(user, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

    }

    /**
     * Checks username and password if its valid from the server.
     *
     * @param response - using the response to see results from server
     * @throws ValidationException - any errors from server throws ValidationException
     */
    public void throwErrorsFromResponse(Response response) throws ValidationException, IOException {
        String jsonData = response.body().string();
        JSONObject serverErrors = new JSONObject(jsonData).getJSONObject("errors");

        System.out.println(String.format("serverErrors are %s", serverErrors.toString()));

        MapBindingResult validationErrors = new MapBindingResult(
            new HashMap<String, String>(),
            UserModel.class.getName()
        );

        if (!serverErrors.isNull("username")) {
            JSONArray usernameErrors = serverErrors.getJSONArray("username");

            for (Object errorString : usernameErrors) {
                validationErrors.rejectValue("username", "server.invalid", errorString.toString());
            }
        }

        if (!serverErrors.isNull("password")) {
            JSONArray passwordErrors = serverErrors.getJSONArray("password");

            for (Object errorString : passwordErrors) {
                validationErrors.rejectValue("password", "server.invalid", errorString.toString());
            }
        }

        throw new ValidationException(validationErrors);
    }
}
