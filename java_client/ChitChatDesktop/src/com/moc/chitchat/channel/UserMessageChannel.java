package com.moc.chitchat.channel;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.channel.listener.NewMessageUserMessageChannelHandler;
import com.moc.chitchat.channel.listener.OkUserMessageChannelHandler;
import com.moc.chitchat.model.UserModel;
import java.io.IOException;
import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.Push;
import org.phoenixframework.channels.Socket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMessageChannel {

    private OkUserMessageChannelHandler okUserMessageChannelHandler;
    private NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler;

    @Autowired
    public UserMessageChannel(
        OkUserMessageChannelHandler okUserMessageChannelHandler,
        NewMessageUserMessageChannelHandler newMessageUserMessageChannelHandler
    ) {
        this.okUserMessageChannelHandler = okUserMessageChannelHandler;
        this.newMessageUserMessageChannelHandler = newMessageUserMessageChannelHandler;
    }

    /**
     *Channle join function.
     * @param socket The socket that we open in order to connect    .
     * @param userModel The userModel as a parameter to our function.
     * @return the channel.
     * @throws IOException that the socket may throws.
     */
    public Channel join(Socket socket, UserModel userModel) throws IOException {
        ObjectNode auth = JsonNodeFactory.instance.objectNode();
        auth.put("authToken", userModel.getAuthToken());
        Channel channel = socket.chan(String.format("user:%s", userModel.getUsername()), auth);

        Push push = channel.join();
        push.receive("ok", okUserMessageChannelHandler);

        channel.on("new:message", this.newMessageUserMessageChannelHandler);

        return channel;
    }
}
