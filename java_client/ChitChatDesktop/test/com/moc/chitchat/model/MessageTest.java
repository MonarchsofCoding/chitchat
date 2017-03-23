package com.moc.chitchat.model;

import com.moc.chitchat.crypto.CryptoFunctions;
import org.junit.Test;

import java.security.KeyPair;

import static org.junit.Assert.assertEquals;

/**
 * MessageTest provides the tests for the Message
 */
public class MessageTest {

    @Test
    public void testConstrutor() {
        UserModel from = new UserModel("Shana");
        UserModel to = new UserModel("Vinny");
        String messageText = "I want to send you a message";
        String encryptedMessage = "Encrypted Message";

        Message message = new Message(from, to, messageText,encryptedMessage);

        assertEquals(from.getUsername(), message.getFrom().getUsername());
        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
        assertEquals(encryptedMessage,message.getEncrypted_message());
    }

    @Test
    public void testSecondConstructor() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";

        Message message = new Message(to, messageText);

        assertEquals(to.getUsername(), message.getTo().getUsername());
        assertEquals(messageText, message.getMessage());
    }

    @Test
    public void testSetTo() {
        UserModel to = new UserModel("David");
        String messageText = "Sending a message!";
        Message message = new Message(to, messageText);

        UserModel receiver = new UserModel("Phil");
        message.setTo(receiver);

        assertEquals(receiver.getUsername(), message.getTo().getUsername());
    }

    @Test
    public void testSetFrom() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        String encryptedMessage = "Encrypted Message";
        Message message = new Message(from, to, messageText,encryptedMessage);

        UserModel sender = new UserModel("Captain");

        message.setFrom(sender);

        assertEquals(sender.getUsername(), message.getFrom().getUsername());
    }

    @Test
    public void testSetMessage() {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";
        Message message = new Message(to, messageText);

        message.setMessage("Halo, the master chief collection!");

        assertEquals("Halo, the master chief collection!", message.getMessage());
    }

    @Test
    public void testToJSONString() throws Exception {
        UserModel to = new UserModel("Gus");
        String messageText = "Sending a message!";

        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair pair = cryptoFunctions.generateKeyPair();
        String encrypted_text = cryptoFunctions.encrypt(messageText, pair.getPublic());
        
        Message message = new Message(to,messageText);
        message.setEncrypted_message(encrypted_text);

        String expectedString = String
                .format(
                        "{\"recipient\":\"%s\",\"message\":\"%s\"}",
                        to.getUsername(),
                        encrypted_text)
                ;
        assertEquals(expectedString, message.toJSONString());
    }

    @Test
    public void testToString() {
        UserModel from = new UserModel("Marine");
        UserModel to = new UserModel("Soldier");
        String messageText = "Run!!!";
        String encryptedMessage = "Encrypted Message";
        Message message = new Message(from, to, messageText,encryptedMessage);

        String expectedString = String.format("%s: %s", from.getUsername(), messageText);

        assertEquals(expectedString, message.toString());
    }
    @Test
    public void testSetEncryptedMessage() {
        UserModel to = new UserModel("Gus");
        String encryptedmessageText = "Sending a message!";
        Message message = new Message(to, encryptedmessageText);

        message.setEncrypted_message("Halo, the master chief collection!");

        assertEquals("Halo, the master chief collection!", message.getEncrypted_message());
    }
}
