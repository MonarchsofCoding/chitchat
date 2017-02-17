package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.UserResolver;
import com.moc.chitchat.validator.UserValidator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by spiros on 14/02/17.
 */

public class LoginControllerTest {

    @Mock private UserResolver mockUserResolver;
    @Mock private UserValidator mockUserValidator;
    @Mock private HttpClient mockHttpClient;
    @Mock private Context mockcontext;
    @Mock private Response.Listener mockResponselistener;
    @Mock private Response.ErrorListener mockErrorListener;

    @InjectMocks
    private LoginController loginController;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor()  {
        assertNotNull(this.loginController);
        assertEquals(
                this.loginController.getClass(), LoginController.class
        );
    }

    @Test
    public void testSuccesfulLoginUser() throws ValidationException{
        UserModel mockUser = mock(UserModel.class);
        //Stub the UserResolver to return UserModel
        when(   this.mockUserResolver.createLoginUser(
                "vjpatel",
                "aaa"
        )


        ).thenReturn(mockUser);

        this.loginController.loginUser (
                mockcontext,
                mockResponselistener,
                mockErrorListener,
                "vjpatel",
                "aaa");


        this.mockHttpClient.sendRequest(mockcontext,
                Request.Method.POST,
                "/api/v1/auth",
                mockUser.toJsonObject(),
                mockResponselistener,
                mockErrorListener);






    }


}
