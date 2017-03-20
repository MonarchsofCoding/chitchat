package com.moc.chitchat.crypto;

import javax.crypto.Cipher;
import java.security.*;
import java.security.PublicKey;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CryptoFunctionsTests provides tests for the CryptoFunction
 */
public class CryptoBoxTest {
    private KeyPairGenerator mockgenerator ;
    private KeyFactory mockkeyFactory;
    private KeyPair mockKeypair;
    private CryptoBox mockCryptoBox;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Cipher mockcipher;
    private String plaintext;


    @Test
    public void testKeyToStringPublic() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        cryptoFunctions.keyToString(userKeyPair.getPublic());
    }

    @Test
    public void testKeyToStringPrivate() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        cryptoFunctions.keyToString(userKeyPair.getPrivate());
    }

    @Test
    public void testStringToKeyPublic() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        String publicKeyText = cryptoFunctions.keyToString(userKeyPair.getPublic());
        PublicKey testKey = cryptoFunctions.pubKeyStringToKey(publicKeyText);

        assertEquals(userKeyPair.getPublic(), testKey);
    }

    @Test
    public void testStringToKeyPrivate() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        String privateKeyText = cryptoFunctions.keyToString(userKeyPair.getPrivate());
        PrivateKey testKey = cryptoFunctions.privKeyStringToKey(privateKeyText);

        assertEquals(userKeyPair.getPrivate(), testKey);
    }

    @Test
    public void testEncryption() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        String message = "I want this message to be encrypted";

        cryptoFunctions.encrypt(message, userKeyPair.getPublic());
    }

    @Test
    public void testDecryption() throws Exception {
        CryptoBox cryptoFunctions = new CryptoBox();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        String expectedMessage = "I want to see this message";

        String encryptedMessage = cryptoFunctions.encrypt(expectedMessage, userKeyPair.getPublic());
        String actualMessage = cryptoFunctions.decrypt(encryptedMessage, userKeyPair.getPrivate());

        assertEquals(expectedMessage, actualMessage);
    }
}
