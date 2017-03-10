package com.moc.chitchat.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.exception.ValidationException;
import com.moc.chitchat.model.MessageModel;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class CurrentChatControllerTest {

    @Mock
    private HttpClient mockHttpClient;
    @Mock
    private Context mockContext;
    @Mock
    private Response.Listener mockResponselistener;
    @Mock
    private Response.ErrorListener mockErrorListener;

    @InjectMocks
    private CurrentChatController currentChatController;
    @InjectMocks
    private CurrentChatConfiguration mockCurrentChatConfiguration;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() throws ValidationException {
        assertNotNull(this.currentChatController);
        assertEquals(
            this.currentChatController.getClass(), CurrentChatController.class
        );
    }

    @Test
    public void testSuccesfulSendMessage() throws ValidationException, JSONException {

        MessageModel mockMessage = mock(MessageModel.class);

        this.currentChatController.sendMessageToRecipient(
            mockContext,
            mockResponselistener,
            mockErrorListener,
            mockMessage
            );


        this.mockHttpClient.sendRequest(
            mockContext,
            Request.Method.POST,
            "/api/v1/messages",
            null,
            mockResponselistener,
            mockErrorListener,
            true
        );


    }


}
