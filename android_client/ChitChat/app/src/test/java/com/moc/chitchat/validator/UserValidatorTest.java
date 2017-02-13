package com.moc.chitchat.validator;

/**
 * Created by aakyo on 13/02/2017.
 */
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.ErrorResponseResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * UserValidatorTest provides tests for the UserValidator
 */

public class UserValidatorTest {
git sta
    @Mock
    private ErrorResponseResolver mockResponse;

    @Before public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateUsernameIsNotEmpty() {
        String username = "";
        UserModel user = new UserModel(username);
        UserValidator userValidator = new UserValidator();

        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            Map<String, List<String>> errors = e.getErrors();
            assertEquals(3, errors.size());
            assertEquals(1, errors.get("username").size());
        }
    }

    @Test
    public void testValidatePasswordIsNotEmpty() {
        String username = "John_Smith";
        String password = "";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        UserValidator userValidator = new UserValidator();

        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            Map<String, List<String>> errors = e.getErrors();
            assertEquals(2, errors.size());
            assertEquals(1, errors.get("password").size());
        }
    }

    @Test
    public void testValidatePasswordCheckEmpty() {
        String username = "John_Smith";
        String password = "password1";
        String passwordCheck = "";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        user.setPasswordCheck(passwordCheck);

        UserValidator userValidator = new UserValidator();

        try {
            userValidator.validate(user);
        } catch (ValidationException e) {
            Map<String, List<String>> errors = e.getErrors();
            assertEquals(1, errors.size());
            assertEquals(1, errors.get("passwordCheck").size());

        }
    }

    @Test
    public void testValidateUser() throws ValidationException {
        String username = "John_Smith";
        String password = "password1";
        String passwordCheck = "password1";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        user.setPasswordCheck(passwordCheck);

        UserValidator userValidator = new UserValidator();
        userValidator.validate(user);
    }
}
