
package com.moc.chitchat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.moc.chitchat.ChitChatApplication;
import com.moc.chitchat.R;
import com.moc.chitchat.application.ChitChatMessagesConfiguration;
import com.moc.chitchat.application.SessionConfiguration;

import javax.inject.Inject;

import org.json.JSONObject;
import org.phoenixframework.channels.*;

import java.util.HashMap;
import java.util.Map;


public class ReceiveMessageService extends Service {

    @Inject
    ChitChatMessagesConfiguration chitChatMessagesConfiguration;
    @Inject
    SessionConfiguration sessionConfiguration;

    Socket socket;
    Channel channel;

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
                + "/api/v1/messages/websocket?guardian_token="
                + sessionConfiguration.getCurrentUser().getAuthToken()).replace("https","ws"));
            socket.connect();

            ObjectNode auth = JsonNodeFactory.instance.objectNode();
            auth.put("guardian_token", sessionConfiguration.getCurrentUser().getAuthToken());

             channel = socket.chan("user:"
                     + sessionConfiguration.getCurrentUser().getUsername(),
                 auth);

            channel.join()
                .receive("ignore", new IMessageCallback() {
                    @Override
                    public void onMessage(Envelope envelope) {
                        System.out.println("IGNORE");
                    }
                })
                .receive("ok", new IMessageCallback() {
                    @Override
                    public void onMessage(Envelope envelope) {
                        System.out.println("JOINED with " + envelope.toString());
                    }
                });

            channel.on("new:message", new IMessageCallback() {
                @Override
                public void onMessage(Envelope envelope) {
                    System.out.println("NEW MESSAGE: " + envelope.toString());
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
