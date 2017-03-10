package com.moc.chitchat.crypto;

import android.util.Base64;

import java.security.*;
import java.security.cert.Extension;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class CryptoBox {

    private KeyPairGenerator generator;
    private KeyFactory converterFactory;

    public CryptoBox() {
        this.generator = null;
        this.converterFactory = null;
    }

    public CryptoBox initialize() {
        try {
            this.generator = KeyPairGenerator.getInstance("RSA");
            this.converterFactory = KeyFactory.getInstance("RSA");
        }
        catch (Exception ex) {
            ex.printStackTrace();
            this.generator = null;
            this.converterFactory = null;
        }
        return this;
    }

    public KeyPair generateKeyPair() throws Exception {
        generator.initialize(4096, new SecureRandom());
        KeyPair pair = generator.generateKeyPair();

        return pair;
    }

    public String keyToString(PublicKey key) {
        return Base64.encodeToString(key.getEncoded(),Base64.DEFAULT);
    }

    public String keyToString(PrivateKey key) {
        return Base64.encodeToString(key.getEncoded(),Base64.DEFAULT);
    }

    public PublicKey pubKeyStringToKey(String pubKey) throws InvalidKeySpecException {
        X509EncodedKeySpec converterSpec = new X509EncodedKeySpec(Base64.decode(pubKey,Base64.DEFAULT));
        return converterFactory.generatePublic(converterSpec);
    }

    public PrivateKey privKeyStringToKey(String privKey) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec converterSpec = new PKCS8EncodedKeySpec(Base64.decode(privKey,Base64.DEFAULT));
        return converterFactory.generatePrivate(converterSpec);
    }

    public String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeToString(cipherText,Base64.DEFAULT);
    }

    public String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decode(cipherText,Base64.DEFAULT);

        Cipher decriptCipher = Cipher.getInstance("RSA");
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8");
    }
}
