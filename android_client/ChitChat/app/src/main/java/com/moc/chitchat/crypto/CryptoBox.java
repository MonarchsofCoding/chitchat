package com.moc.chitchat.crypto;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CryptoBox {

    private KeyPairGenerator generator;
    private KeyFactory converterFactory;

    /**
     * Constructor.
     */
    public CryptoBox() {
        this.generator = null;
        this.converterFactory = null;
    }

    /**
     * Initializes the object.
     * @return The CryptoBox object.
     */
    public CryptoBox initialize() {
        try {
            this.generator = KeyPairGenerator.getInstance("RSA");
            this.converterFactory = KeyFactory.getInstance("RSA");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.generator = null;
            this.converterFactory = null;
        }
        return this;
    }

    /**
     * Generates keys.
     * @return The key pair.
     * @throws Exception in case generation fails.
     */
    public KeyPair generateKeyPair() throws Exception {
        generator.initialize(4096, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    /**
     * Conversion from key to string.
     * @param key The public key.
     * @return The string form of the key.
     */
    public String keyToString(PublicKey key) {
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }

    /**
     * Conversion from key to string.
     * @param key The private key.
     * @return The string form of the key.
     */
    public String keyToString(PrivateKey key) {
        return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
    }

    /**
     * Returns the key object from string
     * @param pubKey public key in string form
     * @return The key object.
     * @throws InvalidKeySpecException if the specification is invalid.
     */
    public PublicKey pubKeyStringToKey(String pubKey) throws InvalidKeySpecException {
        X509EncodedKeySpec converterSpec = new X509EncodedKeySpec(Base64.decode(pubKey, Base64.DEFAULT));
        return converterFactory.generatePublic(converterSpec);
    }

    /**
     * Returns the key object from string
     * @param privKey private key in string form
     * @return The key object.
     * @throws InvalidKeySpecException if the specification is invalid.
     */
    public PrivateKey privKeyStringToKey(String privKey) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec converterSpec = new PKCS8EncodedKeySpec(Base64.decode(privKey, Base64.DEFAULT));
        return converterFactory.generatePrivate(converterSpec);
    }

    /**
     * Encryption.
     * @param plainText The text to encrypt.
     * @param publicKey The public key to encrypt the text.
     * @return The cipher text
     * @throws Exception if the encryption fails.
     */
    public String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeToString(cipherText, Base64.DEFAULT);
    }

    /**
     * Decryption.
     * @param cipherText The cipherText to decipher.
     * @param privateKey The private key to decipher the text.
     * @return The plaintext.
     * @throws Exception if the decipher fails.
     */
    public String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decode(cipherText, Base64.DEFAULT);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8");
    }
}
