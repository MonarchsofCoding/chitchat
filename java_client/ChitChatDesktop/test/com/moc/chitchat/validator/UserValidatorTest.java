package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * UserValidatorTest provides tests for the UserValidator
 */
public class UserValidatorTest {

    @Before
    public void initMocks() {
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
            Errors errors = e.getErrors();
            assertEquals(3, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("username"));
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
            Errors errors = e.getErrors();
            assertEquals(2, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("password"));
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
            Errors errors = e.getErrors();
            assertEquals(1, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("passwordCheck"));

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

    @Test
    public void testSupportsValid() {
        UserValidator userValidator = new UserValidator();
        assertEquals(true, userValidator.supports(UserModel.class));
    }

    @Test
    public void testSupportsInValid() {
        UserValidator userValidator = new UserValidator();
        assertFalse(userValidator.supports(UserResolver.class));
    }

    @Test
    public void testInvalidUsername() {
        String username = "^^^";
        String password = "12345678";
        String passwordCheck = "12345678";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        user.setPasswordCheck(passwordCheck);

        UserValidator userValidator = new UserValidator();
        try {
            userValidator.validate(user);
            fail("This should throw an exception");
        } catch (ValidationException e) {
            Errors errors = e.getErrors();
            assertEquals(1, errors.getFieldErrorCount("username"));
        }
    }
}
