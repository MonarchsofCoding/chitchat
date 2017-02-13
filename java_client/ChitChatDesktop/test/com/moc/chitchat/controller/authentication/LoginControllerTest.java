package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by spiros on 11/02/17.
 */
public class LoginControllerTest {



    @Mock private UserResolver mockUserResolver;
    @Mock private UserValidator mockUserValidator;
    @Mock private HttpClient mockHttpClient;

    @InjectMocks
    private LoginController loginController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.loginController);
        assertEquals(
                this.loginController.getClass(),
                LoginController.class
        );
    }

    @Test
    public void testSuccessfulLoginUser() throws ValidationException, UnirestException, UnexpectedResponseException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createLoginUser(
                        "spiros",
                        "aaa"
                )
        ).thenReturn(mockUser);

        // Create and define the mocked response to return 201 (success)
        HttpResponse<JsonNode> mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus())
                .thenReturn(200);
        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.post("/api/v1/users", mockUser))
                .thenReturn(mockResponse);

        // Run the function to test
        this.loginController.loginUser(
                "spiros",
                "aaa"
        );

    }


    @Test
    public void testServerUnsuccessfulLoginUser() throws UnirestException, ValidationException, UnexpectedResponseException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createLoginUser(
                        "spiros",
                        "aaa"
                )
        ).thenReturn(mockUser);


        // Create and define the mocked response to return 422 (unsuccessful)
        HttpResponse<JsonNode> mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus())
                .thenReturn(401);
        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.post("/api/v1/users", mockUser))
                .thenReturn(mockResponse);

        // Mock the ValidationException
        ValidationException mockValidationException = mock(ValidationException.class);
        when(mockValidationException.getMessage())
                .thenReturn("Validation Exception");

        // Run the function to test
        try {
            this.loginController.loginUser(
                    "spiros",
                    "aaa"
            );
        } catch (ValidationException e) {
            assertEquals("Validation Exception", e.getMessage());
        }

        // Verify the UserValidator.throwErrorsFromResponse was called
        verify(mockUserValidator).throwErrorsFromResponse(mockResponse);
    }

    @Test
    public void testServerErrorLoginUser() throws UnirestException, ValidationException {
        // Stub the UserResolver to return a UserModel
        UserModel mockUser = mock(UserModel.class);
        when(
                this.mockUserResolver.createLoginUser(
                        "spiros",
                        "aaa"
                )
        ).thenReturn(mockUser);


        // Create and define the mocked response to return 401 (unsuccessful)
        HttpResponse<JsonNode> mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus())
                .thenReturn(500);
        // Stub the HTTPClient to return the mocked response
        when(this.mockHttpClient.post("/api/v1/users", mockUser))
                .thenReturn(mockResponse);

        // Run the function to test
        try {
            this.loginController.loginUser(
                    "spiros",
                    "aaa"
            );
        } catch (UnexpectedResponseException e) {
            assertEquals("Unexpected Response code: 500", e.getMessage());
        }

    }

}
