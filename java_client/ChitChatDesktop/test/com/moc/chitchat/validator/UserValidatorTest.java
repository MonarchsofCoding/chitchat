package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;

/**
 * UserValidatorTest provides tests for the UserValidator
 */
public class UserValidatorTest {

    @Test
    public void testValidateUsernameIsNotEmpty()  {
        String username = "";
        UserModel user = new UserModel(username);
        UserValidator userValidator = new UserValidator();

        try{
            userValidator.validate(user);
        }
        catch(ValidationException e)
        {
            Errors errors = e.getErrors();
            assertEquals(3, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("username"));

        }
    }

    @Test
    public void testValidatePasswordIsNotEmpty()
    {
        String username = "John_Smith";
        String password = "";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        UserValidator userValidator = new UserValidator();

        try{
            userValidator.validate(user);
        }
        catch(ValidationException e)
        {
            Errors errors = e.getErrors();
            assertEquals(2, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("password"));

        }
    }

    @Test
    public void testValidatePasswordCheckEmpty()
    {
        String username = "John_Smith";
        String password = "password1";
        String passwordCheck = "";

        UserModel user = new UserModel(username);
        user.setPassword(password);
        user.setPasswordCheck(passwordCheck);

        UserValidator userValidator = new UserValidator();

        try{
            userValidator.validate(user);
        }
        catch(ValidationException e)
        {
            Errors errors = e.getErrors();
            assertEquals(1, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("passwordCheck"));

        }
    }

}
