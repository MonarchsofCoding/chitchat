package com.moc.chitchat.crypto;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoBox {

    private KeyPairGenerator generator;
    private KeyFactory keyFactory;

    private static String cipherAlgorithm = "RSA/ECB/NOPADDING";
    private static String keyFactoryAlgorithm = "RSA";
    private static String keyGeneratorAlgorithm = "RSA";

    /**
     * Constructor.
     */
    public CryptoBox() throws NoSuchAlgorithmException {
        this.generator = KeyPairGenerator.getInstance(CryptoBox.keyGeneratorAlgorithm);
        this.keyFactory = KeyFactory.getInstance(CryptoBox.keyFactoryAlgorithm);
    }

    /**
     * Generates keys.
     * @return The key pair.
     */
    public KeyPair generateKeyPair() {
        generator.initialize(4096, new SecureRandom());

        return generator.generateKeyPair();
    }

    /**
     * Conversion from key to string.
     * @param key The public key.
     * @return The string form of the key.
     */
    public String keyToString(PublicKey key) {
        String enc = Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);

        System.out.println(enc);
        return enc;
    }

    /**
     * Conversion from key to string.
     * @param key The private key.
     * @return The string form of the key.
     */
    public String keyToString(PrivateKey key) {
        return Base64.encodeToString(key.getEncoded(), Base64.NO_WRAP);
    }

    /**
     * Returns the key object from string
     * @param pubKey public key in string form
     * @return The key object.
     * @throws InvalidKeySpecException if the specification is invalid.
     */
    public PublicKey pubKeyStringToKey(String pubKey) throws InvalidKeySpecException {
        X509EncodedKeySpec converterSpec = new X509EncodedKeySpec(Base64.decode(pubKey, Base64.NO_WRAP));
        return keyFactory.generatePublic(converterSpec);
    }

    /**
     * Returns the key object from string
     * @param privKey private key in string form
     * @return The key object.
     * @throws InvalidKeySpecException if the specification is invalid.
     */
    public PrivateKey privKeyStringToKey(String privKey) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec converterSpec = new PKCS8EncodedKeySpec(Base64.decode(privKey, Base64.NO_WRAP));
        return keyFactory.generatePrivate(converterSpec);
    }

    /**
     * Encryption.
     * @param plainText The text to encrypt.
     * @param publicKey The public key to encrypt the text.
     * @return The cipher text
     */
    public String encrypt(String plainText, PublicKey publicKey) throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException{
        Cipher encryptCipher = Cipher.getInstance(CryptoBox.cipherAlgorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeToString(cipherText, Base64.NO_WRAP);
    }

    /**
     * Decryption.
     * @param cipherText The cipherText to decipher.
     * @param privateKey The private key to decipher the text.
     * @return The plaintext.
     */
    public String decrypt(String cipherText, PrivateKey privateKey) throws
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            UnsupportedEncodingException{
        byte[] bytes = Base64.decode(cipherText, Base64.NO_WRAP);

        Cipher decriptCipher = Cipher.getInstance(CryptoBox.cipherAlgorithm);
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8").trim();
    }
}
