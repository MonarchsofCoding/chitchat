package com.moc.chitchat.channel.listener;

import org.junit.Test;
import org.phoenixframework.channels.Envelope;

import static org.mockito.Mockito.mock;

/**
 * OKUserMessageChannelHandlerTest provides tests for the OKUserMessageChannelHandler
 */
public class OKUserMessageChannelHandlerTest {

    @Test
    public void testOnMessage() {
        OKUserMessageChannelHandler okUserMessageChannelHandler = new OKUserMessageChannelHandler();
        Envelope envelope = mock(Envelope.class);
        okUserMessageChannelHandler.onMessage(envelope);
    }

}
