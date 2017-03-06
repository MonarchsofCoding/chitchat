package com.moc.chitchat.channel.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.moc.chitchat.controller.MessageController;
import org.phoenixframework.channels.Envelope;
import org.phoenixframework.channels.IMessageCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewMessageUserMessageChannelHandler implements IMessageCallback {

    private MessageController messageController;

    @Autowired
    public NewMessageUserMessageChannelHandler(
            MessageController messageController
    ) {
        this.messageController = messageController;
    }

    @Override
    public void onMessage(Envelope envelope) {
        JsonNode payload = envelope.getPayload();

        this.messageController.receive(
            payload.get("body").asText(),
            payload.get("from").asText()
        );
    }
}
