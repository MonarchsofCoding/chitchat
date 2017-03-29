package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;


/**
 * MessageValidator provides validation and server error handling of message objects.
 */
@Component
public class MessageValidator implements Validator{


    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Message message = (Message) target;

        byte[] utf8Bytes = new byte[0];
        try {
            utf8Bytes = message.getMessage().getBytes("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        if (utf8Bytes.length > 128) {
            errors.rejectValue("message", "field.required", "too many characters");
        }

    }

    /**
     * Validating the message that the user is about to send to recipient.
     * @param message - message object that we are going to validate
     * @throws ValidationException - if the message holds too many characters
     */
    public void validate(Message message) throws ValidationException {
        MapBindingResult errors = new MapBindingResult(new HashMap<String, String>(), UserModel.class.getName());
        this.validate(message, errors);

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

        if (!serverErrors.isNull("message")) {
            JSONArray messageErrors = serverErrors.getJSONArray("message");

            for (Object errorString : messageErrors) {
                validationErrors.rejectValue("message", "server.invalid", errorString.toString());
            }
        }

        if (!serverErrors.isNull("recipient")) {
            JSONArray messageErrors = serverErrors.getJSONArray("recipient");

            for (Object errorString : messageErrors) {
                validationErrors.rejectValue("recipient", "server.invalid", errorString.toString());
            }
        }

        throw new ValidationException(validationErrors);
    }
}
