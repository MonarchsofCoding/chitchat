package com.moc.chitchat.channel.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.moc.chitchat.controller.MessageController;
import org.junit.Test;
import org.phoenixframework.channels.Envelope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

/**
 * NewMessageUserMessageChannelHandlerTest provides tests for the NewMessageUserMessageChannelHandler
 */
public class NewMessageUserMessageChannelHandlerTest {

    @Test
    public void testConstructor() {
        MessageController messageController = mock(MessageController.class);
        NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler = new
                NewMessageUserMessageChannelHandler(messageController);
        assertNotNull(messageController);
        assertEquals(NewMessageUserMessageChannelHandler.class, newMessageUserMessageChannelHandler.getClass());
    }

    @Test
    public void testOnMessage() {
        MessageController messageController = mock(MessageController.class);
        NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler = new
                NewMessageUserMessageChannelHandler(messageController);
        Envelope envelope = mock(Envelope.class);
        JsonNode payload = mock(JsonNode.class);
        JsonNode body = mock(JsonNode.class);
        JsonNode from = mock(JsonNode.class);

        when(envelope.getPayload()).thenReturn(payload);
        when(payload.get("body")).thenReturn(body);
        when(payload.get("from")).thenReturn(from);

        newMessageUserMessageChannelHandler.onMessage(envelope);
    }

}
