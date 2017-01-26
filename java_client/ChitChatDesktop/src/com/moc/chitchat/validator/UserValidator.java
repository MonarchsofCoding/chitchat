package com.moc.chitchat.validator;


import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.view.authentication.RegistrationView;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import javax.swing.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.M;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserModel user = (UserModel) target;
        String regexpress = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+!=])(?=\\S+$).{8,}$";




        if (user.getUsername().isEmpty()) {
            errors.rejectValue("username", "field.required");
        }

        if (user.getPassword().isEmpty()) {
            errors.rejectValue("password", "field.required");
        }
        if (user.getPassword().matches(regexpress)){}
        else errors.rejectValue("passwordType", "password.wrong.format");


        if (!user.getPasswordCheck().equals(user.getPassword())) {

            errors.rejectValue("passwordCheck", "password.mismatch");
        }

    }

    public void validate(UserModel user) throws ValidationException {
        MapBindingResult errors = new MapBindingResult(new HashMap<String,String>(), UserModel.class.getName());

        this.validate(user, errors);
        System.out.println("leleme   "+errors.hasFieldErrors("username"));
        if (errors.hasErrors()) {

            if(errors.hasFieldErrors("username"))JOptionPane.showMessageDialog(null,"The field of username is empty");
            else if(errors.hasFieldErrors("password"))JOptionPane.showMessageDialog(null,"Password is empty");
            else if(errors.hasFieldErrors("passwordType")) JOptionPane.showMessageDialog(null,"Password must be at least 8 character long and to contain: \n"+" numbers, uper&low case characters,1 special symbols(@#$%^&*+!=])");
            else if(errors.hasFieldErrors("passwordCheck")) JOptionPane.showMessageDialog(null,"Passwords do not match!");
            //else TODO: send the http message to the server
        }
    }
}
