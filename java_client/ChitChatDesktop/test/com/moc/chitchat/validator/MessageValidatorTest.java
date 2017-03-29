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

    /**
     *  Test with 128 characters
     */
    @Test
    public void testValidationLargeAmountChars() {
        UserModel user = new UserModel("Ben");
        // 128 characters
        String msg = "12345678912345678912345678912345678912345678912345678912345678912345678912345678912345678912345678912345678912345678912345678912";
        System.out.println(msg.length());
        Message message = new Message(user, msg);

        MessageValidator messageValidator = new MessageValidator();
        try {
            messageValidator.validate(message);
        } catch (ValidationException e) {
            fail(String.format("the string - \"%s\" should of passed", msg));
        }
    }

    @Test
    public void testValidationFail() {
        UserModel user = new UserModel("Ben");
        // 194 characters
        String msg = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789";
        Message message = new Message(user, msg);

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
