package com.moc.chitchat.view.helper;


import com.moc.chitchat.application.Configuration;
import com.moc.chitchat.client.HttpClient;
import com.moc.chitchat.crypto.CryptoFunctions;
import com.moc.chitchat.model.Message;
import com.moc.chitchat.model.UserModel;
import com.moc.chitchat.resolver.MessageResolver;
import com.moc.chitchat.resolver.UserResolver;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * MessageHelper provides helper methods involved with sending messages.
 */
public class MessageHelper {
    /**
     * Sends a message using HTTP from the given username to the given username with the given message.
     *
     * @param fromUsername The username to send from.
     * @param fromPass     The password to authenticate with.
     * @param toUsername   The username to send to.
     * @param message      The message to send.
     * @throws IOException
     */
    public static void sendMessage(String fromUsername, String fromPass, String toUsername, String message) throws Exception {
        Configuration c = new Configuration();
        if (System.getenv("CHITCHAT_ENV") != null && System.getenv("CHITCHAT_ENV").length() > 0) {
            c.setTestingMode();
        } else {
            c.setDevelopmentMode();
        }

        HttpClient client2 = new HttpClient(c);
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        // Create the User object from parameters.
        UserModel fromUser = new UserModel(fromUsername);
        fromUser.setPassword(fromPass);
        fromUser.setPublicKey(userKeyPair.getPublic());
        fromUser.setPrivatekey(userKeyPair.getPrivate());
        Response r = client2.post("/api/v1/auth", fromUser);

        JSONObject jsonResp = new JSONObject(r.body().string());
        String authToken = jsonResp.getJSONObject("data").getString("authToken");
        fromUser.setAuthToken(authToken);
        c.setLoggedInUser(fromUser);


        HttpClient client = new HttpClient(c);
        Response userResp = client.get(String.format("/api/v1/users/%s", toUsername), new HashMap<String, Object>());

        String jsonData = userResp.body().string();
        JSONObject respJsonObj = new JSONObject(jsonData);

        UserResolver userResolver = new UserResolver();
        UserModel toUser = userResolver.getUserModelViaJSonObject(respJsonObj.getJSONObject("data"));

        HttpClient client3 = new HttpClient(c);
        String messageencrypt = cryptoFunctions.encrypt(message, toUser.getPublicKey());
        MessageResolver messageResolver= new MessageResolver();
        Configuration configuration = new Configuration();
        Message newMessage = messageResolver.createMessage(fromUser,
                toUser,message,messageencrypt);
        client3.post("/api/v1/messages", newMessage);

        r.body().close();
        userResp.body().close();
    }

    public static void loginUser( String toUsername,String toPassword) throws Exception {
        Configuration c = new Configuration();
        if (System.getenv("CHITCHAT_ENV") != null && System.getenv("CHITCHAT_ENV").length() > 0) {
            c.setTestingMode();
        } else {
            c.setDevelopmentMode();
        }
        HttpClient client2 = new HttpClient(c);


        //Create the public and private keys
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        // Create the User object from parameters.
        UserModel user2 = new UserModel(toUsername);
        user2.setPassword(toPassword);
        user2.setPublicKey(userKeyPair.getPublic());
        user2.setPrivatekey(userKeyPair.getPrivate());

        client2.post("/api/v1/auth", user2).body().close();

    }
}