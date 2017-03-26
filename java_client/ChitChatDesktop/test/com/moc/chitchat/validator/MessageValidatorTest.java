package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * MessageValidatorTest provides tests for the MessageValidator
 */
public class MessageValidatorTest {

    @Test
    public void testValidationSuccess() {
        UserModel user = new UserModel("Ben");
        Message message = new Message(user, "my message");

        MessageValidator messageValidator = new MessageValidator();
        try {
            messageValidator.validate(message);
        } catch (ValidationException e) {
            fail("the string - \"my message\" should of passed");
        }
    }

    @Test
    public void testValidationFail() {
        UserModel user = new UserModel("Ben");
        Message message = new Message(user, "123456789123456789123456789123456789123456789123456789123456789123456789123456789");

        MessageValidator messageValidator = new MessageValidator();
        try {
            messageValidator.validate(message);
            fail("should of reached validation exception");
        } catch (ValidationException e) {
            Errors errors = e.getErrors();
            assertEquals(1, errors.getErrorCount());
            assertEquals(1, errors.getFieldErrorCount("message"));
        }
    }

}
