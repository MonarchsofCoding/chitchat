package com.moc.chitchat.crypto;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static sun.management.jmxremote.ConnectorBootstrap.initialize;

/**
 * Created by spiros on 12/03/2017.
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

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        mockCryptofunctions = Mockito.mock(CryptoFunctions.class);
        mockgenerator = KeyPairGenerator.getInstance("RSA");
        mockgenerator.initialize(4096,new SecureRandom());
        mockkeyFactory = KeyFactory.getInstance("RSA");

    }


    /**
     * Constructor for CryptoFunction.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testConstructor() throws NoSuchAlgorithmException {
        mockgenerator = KeyPairGenerator.getInstance("RSA");
        mockkeyFactory = KeyFactory.getInstance("RSA");

        KeyPairGenerator keyPairGenerator = mockgenerator;
        KeyFactory keyFactory = mockkeyFactory;

        assertEquals(mockgenerator, keyPairGenerator);
        assertEquals(mockkeyFactory, keyFactory);
    }

    /**
     * Generate KeyPair of PublicKey and PrivateKey.
     * @throws Exception
     */
    @Test
    public void testKeyPairgenerator() throws Exception {


        mockCryptofunctions.initialize();
        mockKeypair = this.mockgenerator.generateKeyPair();
        assertNotNull(mockKeypair);


    }

    /**
     * Test that the publicKey is converted from String to Key.
     * @throws InvalidKeySpecException
     */
    @Test
    public void TestPublicKeyStringToKey() throws InvalidKeySpecException {
        mockKeypair = this.mockgenerator.generateKeyPair();
        publicKey = mockKeypair.getPublic();
        String pkey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        X509EncodedKeySpec converterSpec = new X509EncodedKeySpec(Base64.getDecoder().decode(pkey));
        assertEquals(publicKey,mockkeyFactory.generatePublic(converterSpec));
    }


    /**
     * Test that the Public key converted to string.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testPublickeyToString() throws NoSuchAlgorithmException {
        mockKeypair = this.mockgenerator.generateKeyPair();
        publicKey = mockKeypair.getPublic();
        String pkey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        assertNotNull(pkey);

    }
    /**
     * Test that the privateKey is converted from String to Key.
     * @throws InvalidKeySpecException
     */
    @Test
    public void TestPrivateKeyStringToKey() throws InvalidKeySpecException {
        mockKeypair = this.mockgenerator.generateKeyPair();
        privateKey = mockKeypair.getPrivate();
        String prkey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        PKCS8EncodedKeySpec converterSpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prkey));
        assertEquals(privateKey,mockkeyFactory.generatePrivate(converterSpec));
    }


    /**
     * Test that the Private key converted to string.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testPrivatekeyToString() throws NoSuchAlgorithmException {
        mockKeypair = this.mockgenerator.generateKeyPair();
        privateKey = mockKeypair.getPrivate();
        String prkey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        assertNotNull(prkey);

    }

    /**
     * We test the Encryption and Decryption functions together.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    @Test
    public void testEncryptionAndDecryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
       plaintext = "hello";
       mockcipher = Cipher.getInstance("RSA");
       mockKeypair = this.mockgenerator.generateKeyPair();
       publicKey = mockKeypair.getPublic();
       privateKey = mockKeypair.getPrivate();
       mockcipher.init(Cipher.ENCRYPT_MODE,publicKey);
       byte[] cipherText = mockcipher.doFinal(plaintext.getBytes("UTF-8"));
       String cipherTextToString = Base64.getEncoder().encodeToString(cipherText);
       assertNotNull(cipherTextToString);

       Cipher decriptCipher = Cipher.getInstance("RSA");
       byte[] plaintxt = Base64.getDecoder().decode(cipherTextToString);
       decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
       String test = new String(decriptCipher.doFinal(plaintxt),"UTF-8");
       assertEquals(plaintext,test);
    }


}
