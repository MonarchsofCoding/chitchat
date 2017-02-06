package com.moc.chitchat.validator;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import java.util.HashMap;

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
            errors.rejectValue("username", "field.required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "field.required");
        }

        if (user.getPasswordCheck() == null || !user.getPasswordCheck().equals(user.getPassword())) {
            errors.rejectValue("passwordCheck", "password.mismatch");
        }
    }

    public void validate(UserModel user) throws ValidationException {
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());
        this.validate(user, errors);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

    }

    public void throwErrorsFromResponse(HttpResponse<JsonNode> response) throws ValidationException
    {
        JSONObject serverErrors = response.getBody().getObject().getJSONObject("errors");

        MapBindingResult validationErrors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());


        if (!serverErrors.isNull("username")) {
            JSONArray usernameErrors = serverErrors.getJSONArray("username");

            for (Object errorString:usernameErrors) {
                validationErrors.rejectValue("username", errorString.toString());
            }
        }

        if (!serverErrors.isNull("password")){
            JSONArray passwordErrors = serverErrors.getJSONArray("password");

            for (Object errorString:passwordErrors){
                validationErrors.rejectValue("password", errorString.toString());
            }
        }
        throw new ValidationException(validationErrors);


    }
}
