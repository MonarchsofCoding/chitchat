
package com.moc.chitchat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.activity.SearchUserActivity;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import javax.inject.Inject;

import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.ChannelEvent;
import org.phoenixframework.channels.Envelope;
import org.phoenixframework.channels.IMessageCallback;
import org.phoenixframework.channels.Socket;

public class ReceiveMessageService extends Service {

    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    SessionConfiguration sessionConfiguration;
    @Inject
    CryptoBox cryptoBox;

    Socket socket;
    Channel channel;

    Context serviceContext = this;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((ChitChatApplication) this.getApplication()).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            socket = new Socket((this.getResources().getString(R.string.server_url)
                + "/api/v1/messages/websocket?authToken="
                + sessionConfiguration.getCurrentUser().getAuthToken()).replace("https", "ws"));
            socket.connect();

            ObjectNode auth = JsonNodeFactory.instance.objectNode();
            auth.put("authToken", sessionConfiguration.getCurrentUser().getAuthToken());

            channel = socket.chan("user:"
                    + sessionConfiguration.getCurrentUser().getUsername(),
                auth);

            channel.join()
                .receive("ignore", new IMessageCallback() {
                    @Override
                    public void onMessage(Envelope envelope) {
                        //IGNORE.
                    }
                })
                .receive("ok", new IMessageCallback() {
                    @Override
                    public void onMessage(Envelope envelope) {
                        //Receiving callback to accept the acceptance.
                    }
                });

            channel.on("new:message", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    String cipherMessage = envelope.getPayload().get("body").asText();
                    String message = null;
                    try {
                        message = cryptoBox.decrypt(
                            cipherMessage,
                            sessionConfiguration.getCurrentUser().getPrivateKey()
                        );
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        message = "\"ChitChat: Message couldn't decrypted.\"";
                    }
                    String from = envelope.getPayload().get("from").asText();
                    UserModel fromUser = new UserModel(from);
                    MessageModel toAdd = new MessageModel(
                        fromUser,
                        sessionConfiguration.getCurrentUser(),
                        message
                    );
                    chitChatMessagesConfiguration.addMessageToConversation(
                        fromUser,
                        toAdd,
                        false
                    );
                    System.out.println("Message from " + fromUser.getUsername() + " is received.");
                    System.out.println("The received message: " + message);
                }
            });

            channel.on(ChannelEvent.CLOSE.getPhxEvent(), new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    System.out.println("CLOSED: " + envelope.toString());
                }
            });

            channel.on(ChannelEvent.ERROR.getPhxEvent(), new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    System.out.println("ERROR: " + envelope.toString());
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
