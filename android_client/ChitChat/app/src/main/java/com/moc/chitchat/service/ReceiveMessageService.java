
package com.moc.chitchat.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.CurrentChatConfiguration;
import com.moc.chitchat.application.SessionConfiguration;
import com.moc.chitchat.crypto.CryptoBox;
import com.moc.chitchat.model.MessageModel;
import com.moc.chitchat.model.UserModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;

import org.phoenixframework.channels.Channel;
import org.phoenixframework.channels.ChannelEvent;
import org.phoenixframework.channels.Envelope;
import org.phoenixframework.channels.IMessageCallback;
import org.phoenixframework.channels.Socket;

public class ReceiveMessageService extends Service{

    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    SessionConfiguration sessionConfiguration;
    @Inject
    CurrentChatConfiguration currentChatConfiguration;
    @Inject
    CryptoBox cryptoBox;

    Socket socket;
    Channel channel;

    Service receiveMessageService = this;

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
                + sessionConfiguration.getCurrentUser().getAuthToken()).replace("http", "ws"));
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
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

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
                    } catch (
                        NoSuchPaddingException
                        | NoSuchAlgorithmException
                        | InvalidKeyException
                        | IllegalBlockSizeException
                        | BadPaddingException
                        | UnsupportedEncodingException exc
                    ) {
                        exc.printStackTrace();
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

        channel.on("user:logout", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    currentChatConfiguration.cleanCurrentRecipient();
                    chitChatMessagesConfiguration.clearChitChatMessagesConfiguration();
                    if(sessionConfiguration.getCurrentActivity() != null) {
                        sessionConfiguration.getCurrentActivity().finish();
                    }
                    NotificationCompat.Builder builder = new NotificationCompat
                        .Builder(receiveMessageService);
                    builder
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentTitle("Session Terminated")
                        .setContentText("Your account is used to log in from another device.")
                        .setPriority(Notification.PRIORITY_MAX);
                    NotificationManager logoutNotificationManager
                        = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    logoutNotificationManager.notify(0, builder.build());
                    receiveMessageService.stopSelf();
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

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    channel.leave();
                    socket.disconnect();
                    System.out.println("Service for receiving message gracefully stopped.");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }
}
