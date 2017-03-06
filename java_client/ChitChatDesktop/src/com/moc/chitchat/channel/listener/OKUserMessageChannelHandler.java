package com.moc.chitchat.channel.listener;

import org.phoenixframework.channels.Envelope;
import org.phoenixframework.channels.IMessageCallback;
import org.springframework.stereotype.Component;

@Component
public class OKUserMessageChannelHandler implements IMessageCallback {

    @Override
    public void onMessage(Envelope envelope) {
        System.out.println("Connected!");
    }
}
