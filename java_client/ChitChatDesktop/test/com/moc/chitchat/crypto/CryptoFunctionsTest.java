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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
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
        mockCryptofunctions = new CryptoFunctions();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        generator.initialize(4096,new SecureRandom());
        publicKey = mock(PublicKey.class);
        privateKey = mock(PrivateKey.class);

    }


    /**
     * Constructor for CryptoFunction.
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testConstructor() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = mockgenerator;
        KeyFactory keyFactory = mockkeyFactory;

        assertEquals(mockgenerator, keyPairGenerator);
        assertNotNull(mockgenerator);
        assertEquals(mockkeyFactory, keyFactory);
        assertNotNull(mockkeyFactory);
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
        assertThat(mockkeyFactory.generatePublic(converterSpec),instanceOf(PublicKey.class));
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
        assertThat(pkey,instanceOf(String.class));

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
        assertThat(mockkeyFactory.generatePrivate(converterSpec),instanceOf(PrivateKey.class));
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
        assertThat(prkey,instanceOf(String.class));

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
       assertThat(cipherTextToString,instanceOf(String.class));

       Cipher decriptCipher = Cipher.getInstance("RSA");
       byte[] plaintxt = Base64.getDecoder().decode(cipherTextToString);
       decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);
       String test = new String(decriptCipher.doFinal(plaintxt),"UTF-8");
       assertEquals(plaintext,test);
    }


}
