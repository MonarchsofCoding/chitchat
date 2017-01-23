package com.moc.chitchat.validator;


import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
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

        if (user.getPassword().isEmpty()) {
            errors.rejectValue("password", "field.required");
        }

        if (user.getPasswordCheck().equals(user.getPassword())) {
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
}
