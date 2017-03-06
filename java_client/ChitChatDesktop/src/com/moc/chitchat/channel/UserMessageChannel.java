package com.moc.chitchat.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.channel.listener.NewMessageUserMessageChannelHandler;
import com.moc.chitchat.channel.listener.OKUserMessageChannelHandler;
import com.moc.chitchat.model.UserModel;
import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.Push;
import org.phoenixframework.channels.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserMessageChannel {

    private OKUserMessageChannelHandler okUserMessageChannelHandler;
    private NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler;

    @Autowired
    public UserMessageChannel(
        OKUserMessageChannelHandler okUserMessageChannelHandler,
        NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler
    ) {
        this.okUserMessageChannelHandler = okUserMessageChannelHandler;
        this.newMessageUserMessageChannelHandler = newMessageUserMessageChannelHandler;
    }

    public Channel join(Socket socket, UserModel userModel) throws IOException {
        ObjectNode auth = JsonNodeFactory.instance.objectNode();
        auth.put("authToken", userModel.getAuthToken());
        Channel channel = socket.chan(String.format("user:%s", userModel.getUsername()), auth);

        Push p = channel.join();
        p.receive("ok", okUserMessageChannelHandler);

        channel.on("new:message", this.newMessageUserMessageChannelHandler);

        return channel;
    }
}
