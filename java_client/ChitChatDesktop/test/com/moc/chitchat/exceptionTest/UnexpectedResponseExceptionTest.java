package com.moc.chitchat.exceptionTest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.moc.chitchat.exception.UnexpectedResponseException;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * UnexpectedResponseExceptionTest provides Tests for the UnexpectedResponseException
 */
public class UnexpectedResponseExceptionTest {

    @Mock
    private HttpResponse<JsonNode> mockResponse;

    @Test
    public void testGetResponse()
    {
        // Getting the unexpected response for status code 500
        mockResponse = (HttpResponse<JsonNode>) mock(HttpResponse.class);
        when(mockResponse.getStatus()).thenReturn(500);

        UnexpectedResponseException unexpected = new UnexpectedResponseException(mockResponse);

        assertEquals(mockResponse.getStatus(), unexpected.getResponse().getStatus());
    }
}
