package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserValidator provides the methods involved with performing local validation on a UserModel.
 */
public class UserValidator {

    /**
     * validate validates a given User.
     * @param user the User to be validated.
     * @throws ValidationException
     */
    public void validate(UserModel user) throws ValidationException {

        Map<String, List<String>> errors = new HashMap<>();
        List<String> usernameErrors = new ArrayList<>();
        List<String> passwordErrors = new ArrayList<>();
        List<String> passwordCheckErrors = new ArrayList<>();

        if (user.getUsername().isEmpty()) {
            usernameErrors.add("Username cannot be empty.");
            errors.put("username", usernameErrors);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            passwordErrors.add("Password cannot be empty.");
            errors.put("password", passwordErrors);
        }

        if (user.getPasswordCheck() == null || !user.getPasswordCheck().equals(user.getPassword())) {
            passwordCheckErrors.add("The passwords must match,");
            errors.put("passwordCheck", passwordCheckErrors);
        }

        if (errors.size() > 0) {
            throw new ValidationException(errors);
        }
    }
}