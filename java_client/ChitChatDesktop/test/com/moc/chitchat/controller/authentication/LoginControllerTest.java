package com.moc.chitchat.controller.authentication;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.client.WebSocketClient;
import com.moc.chitchat.exception.UnexpectedResponseException;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.xml.ws.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 *  LoginControllerTest provides test for the LoginController
 */
public class LoginControllerTest {



    @Mock private UserResolver mockUserResolver;
    @Mock private UserValidator mockUserValidator;
    @Mock private HttpClient mockHttpClient;
    @Mock private Configuration configuration;
    @Mock private WebSocketClient webSocketClient;

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
    public void testSuccessfulLoginUser() throws ValidationException, UnirestException, UnexpectedResponseException, IOException, InterruptedException {
        String validUsername = "spiros";
        String validPassword = "abcde1234";

        // Set up mock server
        MockWebServer server = new MockWebServer();

        // Schedule the valid response
        MockResponse mockResponse = new MockResponse();

        // Not used, but for completeness
        String jsonResponse = "{" +
                "\"data\": {" +
                "\"username\": \"spiros\"" +","+
                "\"exp\":" +","+
                "\"authToken\":" +
                "}" +
                "}";

        mockResponse
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "bearer ")
                .setBody(jsonResponse)
                .setResponseCode(200)
        ;

        server.enqueue(mockResponse);

        HttpUrl baseUrl = server.url("/");

        // Set up controller
        UserResolver userResolver = new UserResolver();
        UserValidator userValidator = new UserValidator();
        WebSocketClient mockwebSocketClient = mock(WebSocketClient.class);
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getBackendAddress()).thenReturn(baseUrl.toString());
        HttpClient httpClient = new HttpClient(mockConfiguration);

        LoginController loginController = new LoginController(
                userResolver,
                httpClient,
                userValidator,
                mockConfiguration,
                mockwebSocketClient

        );

        try {
            loginController.loginUser(
                    validUsername,
                    validPassword
            );

        } catch (ValidationException | UnexpectedResponseException e) {
            fail();
            e.printStackTrace();
        }

        // assert requests
        RecordedRequest recordedRequest = null;
        try {
            recordedRequest = server.takeRequest();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("//api/v1/auth", recordedRequest.getPath());
        assertEquals("POST", recordedRequest.getMethod());
       // assertEquals("data",recordedRequest.getBody());
        //assertEquals(jsonResponsegetJSONObject("data").get("authToken").toString(),recordedRequest.getBody(jsonResponse.contains("authToken")));
        server.shutdown();
    }

     /*



    }
    private static MockResponse BaseResponse(int responCode){
        MockResponse response = new MockResponse()
                .setResponseCode(401)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("Cache-Control", "no-cache")
                .setBody("{}");
        return response;
    }*/


   /* @Test
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
        when(this.mockHttpClient.post("/api/v1/auth", mockUser))
                .thenReturn(mockResponse);

        // Mock the ValidationException
        ValidationException mockValidationException = mock(ValidationException.class);
        when(mockValidationException.getMessage())
                .thenReturn("Validation Exception");

        // Mock the authorisation token
        JsonNode bodyResponse = mock(JsonNode.class);
        when(mockResponse.getBody()).thenReturn(bodyResponse);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("authToken", "some_string");

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("data", jsonObject);

        when(bodyResponse.getObject()).thenReturn(bodyJson);

        // Run the function to test
        try {
            this.loginController.loginUser(
                    "spiros",
                    "aaa"
            );
        } catch (ValidationException e) {
            assertEquals("Validation Exception", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
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
        when(this.mockHttpClient.post("/api/v1/auth", mockUser))
                .thenReturn(mockResponse);

        // Run the function to test
        try {
            this.loginController.loginUser(
                    "spiros",
                    "aaa"
            );
        } catch (UnexpectedResponseException e) {
            assertEquals("Unexpected Response code: 500", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
