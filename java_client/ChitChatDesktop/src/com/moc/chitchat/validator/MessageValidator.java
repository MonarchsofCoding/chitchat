package com.moc.chitchat.validator;

import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.validation.MapBindingResult;


/**
 * MessageValidator provides validation and server error handling of message objects.
 */
@Component
public class MessageValidator {

    /**
     * Checks username and password if its valid from the server.
     *
     * @param response - using the response to see results from server
     * @throws ValidationException - any errors from server throws ValidationException
     */
    public void throwErrorsFromResponse(Response response) throws ValidationException, IOException {
        String jsonData = response.body().string();
        JSONObject serverErrors = new JSONObject(jsonData).getJSONObject("errors");

        System.out.println("serverErrors are " + serverErrors.toString());

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
