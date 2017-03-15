package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchUserControllerTest {

    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private Context mockContext;
    @Mock
    private Response.Listener mockResponselistener;
    @Mock
    private Response.ErrorListener mockErrorListener;

    @InjectMocks
    private SearchUserController searchUserController;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() throws ValidationException {
        assertNotNull(this.searchUserController);
        assertEquals(
            this.searchUserController.getClass(), SearchUserController.class
        );
    }

    @Test
    public void testSuccesfulSearchUser() throws ValidationException {

        this.searchUserController.searchUser(
            mockContext,
            mockResponselistener,
            mockErrorListener,
            "spi"
        );


        this.mockHttpClient.sendRequest(mockContext,
            Request.Method.POST,
            "/api/v1/users?username=" + "spi",
            null,
            mockResponselistener,
            mockErrorListener,
            true
        );


    }


}
