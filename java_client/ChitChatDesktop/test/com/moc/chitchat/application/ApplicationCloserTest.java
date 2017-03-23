package com.moc.chitchat.application;

import com.moc.chitchat.client.WebSocketClient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * ApplicationCloserTest provides tests for the application closer
 */
public class ApplicationCloserTest {

    @Test
    public void testConstructor() {
        WebSocketClient mockWebSocketClient = mock(WebSocketClient.class);
        ApplicationCloser applicationCloser = new ApplicationCloser(mockWebSocketClient);

        assertNotNull(applicationCloser);
        assertEquals(ApplicationCloser.class, applicationCloser.getClass());
    }



}
