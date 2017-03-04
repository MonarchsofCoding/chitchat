package com.moc.chitchat.validator;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UserValidatorTest provides tests for the UserValidator
 */
public class UserValidatorTest {

    @Mock
    private HttpResponse<JsonNode> mockResponse;

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
    public void testDuplicateUser() {
        this.mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);

        JsonNode thebody = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(thebody);

        JSONArray usernameErrors = new JSONArray();
        usernameErrors.put("duplicate username");

        JSONObject userErrors = new JSONObject();
        userErrors.put("username", usernameErrors);

        JSONObject obj = new JSONObject();
        obj.put("errors", userErrors);

        when(thebody.getObject()).thenReturn(obj);

        UserValidator userValidator = new UserValidator();
        try {
            userValidator.throwErrorsFromResponse(this.mockResponse);
        } catch (ValidationException e) {
            Errors errors = e.getErrors();
            assertEquals(1, errors.getErrorCount());
            assertEquals("duplicate username", errors.getFieldError().getDefaultMessage());
        }
    }

    @Test
    public void testInvalidPassword() {
        this.mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);

        JsonNode thebody = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(thebody);

        JSONArray passwordsErrors = new JSONArray();
        passwordsErrors.put("1234");

        JSONObject pwdErrors = new JSONObject();
        pwdErrors.put("password", passwordsErrors);

        JSONObject obj = new JSONObject();
        obj.put("errors", pwdErrors);

        when(thebody.getObject()).thenReturn(obj);

        UserValidator userValidator = new UserValidator();
        try {
            userValidator.throwErrorsFromResponse(this.mockResponse);
        } catch (ValidationException e) {
            Errors errors = e.getErrors();
            assertEquals(1, errors.getErrorCount());
            assertEquals("1234", errors.getFieldError().getDefaultMessage());
        }
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
    public void testServerErrorMessage() {
        JsonNode thebody = mock(JsonNode.class);
        when(this.mockResponse.getBody()).thenReturn(thebody);

        JSONArray serverErrors = new JSONArray();
        serverErrors.put("message");

        JSONObject servErrors = new JSONObject();
        servErrors.put("message", serverErrors);

        JSONObject obj = new JSONObject();
        obj.put("errors", servErrors);

        when(thebody.getObject()).thenReturn(obj);

        try {
            UserValidator userValidator = new UserValidator();
            userValidator.throwErrorsFromResponse(mockResponse);
        } catch (ValidationException validationException) {
            Errors errors = validationException.getErrors();
            assertEquals(1, errors.getFieldErrorCount());
        }
    }


}
