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

public class SearchUserControllerTest {

    @Mock private HttpClient mockHttpClient;
    @Mock private Context mockContext;
    @Mock private Response.Listener mockResponselistener;
    @Mock private Response.ErrorListener mockErrorListener;

    @InjectMocks
    private SearchUserController searchUserController;

    @Before
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() throws ValidationException {
        assertNotNull(this.searchUserController);
        assertEquals(
            this.searchUserController.getClass(), SearchUserController.class
        );
    }

    //TODO: Don't know how to mock the result or how to be sure the functionality is tested -Aydin
    @Test
    public void testSuccesfulSearchUser() throws ValidationException{

        this.searchUserController.searchUser (
            mockContext,
            mockResponselistener,
            mockErrorListener,
            "spi",
            null);


        this.mockHttpClient.sendRequestWithHeader(mockContext,
            Request.Method.POST,
            "/api/v1/users?username=" + "spi",
            null,
            mockResponselistener,
            mockErrorListener,
            null);


    }



}
