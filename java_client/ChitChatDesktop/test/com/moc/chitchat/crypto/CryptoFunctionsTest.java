package com.moc.chitchat.crypto;

import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * CryptoFunctionsTests provides tests for the CryptoFunction
 */
public class CryptoFunctionsTest {
    private KeyPairGenerator mockgenerator ;
    private KeyFactory mockkeyFactory;
    private KeyPair mockKeypair;
    private CryptoFunctions  mockCryptofunctions;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Cipher mockcipher;
    private String plaintext;


    @Test
    public void testKeyToStringPublic() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        cryptoFunctions.keyToString(userKeyPair.getPublic());
    }

    @Test
    public void testKeyToStringPrivate() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        cryptoFunctions.keyToString(userKeyPair.getPrivate());
    }

    @Test
    public void testStringToKeyPublic() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        String publicKeyText = cryptoFunctions.keyToString(userKeyPair.getPublic());
        PublicKey testKey = cryptoFunctions.pubKeyStringToKey(publicKeyText);

        assertEquals(userKeyPair.getPublic(), testKey);
    }

    @Test
    public void testStringToKeyPrivate() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();

        String privateKeyText = cryptoFunctions.keyToString(userKeyPair.getPrivate());
        PrivateKey testKey = cryptoFunctions.privKeyStringToKey(privateKeyText);

        assertEquals(userKeyPair.getPrivate(), testKey);
    }

    @Test
    public void testEncryption() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        String message = "I want this message to be encrypted";

        cryptoFunctions.encrypt(message, userKeyPair.getPublic());
    }

    @Test
    public void testDecryption() throws Exception {
        CryptoFunctions cryptoFunctions = new CryptoFunctions();
        KeyPair userKeyPair = cryptoFunctions.generateKeyPair();
        String expectedMessage = "I want to see this message";

        String encryptedMessage = cryptoFunctions.encrypt(expectedMessage, userKeyPair.getPublic());
        String actualMessage = cryptoFunctions.decrypt(encryptedMessage, userKeyPair.getPrivate());

        assertEquals(expectedMessage, actualMessage);
    }
}
