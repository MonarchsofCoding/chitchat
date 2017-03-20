package com.moc.chitchat.crypto;

import org.apache.commons.codec.binary.Base64;

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

import javax.crypto.Cipher;

public class CryptoBox {

    private KeyPairGenerator generator;
    private KeyFactory keyFactory;

    private static String cipherAlgorithm = "RSA/ECB/NOPADDING";
    private static String keyFactoryAlgorithm = "RSA";
    private static String keyGeneratorAlgorithm = "RSA";

    /**
     * CryptoFunctions constructor.
     * @throws NoSuchAlgorithmException No such Algorithm Security Exception if RSA doesn't exist.
     */
    public CryptoBox() throws NoSuchAlgorithmException {
        this.generator = KeyPairGenerator.getInstance(CryptoBox.keyGeneratorAlgorithm);
        this.keyFactory = KeyFactory.getInstance(CryptoBox.keyFactoryAlgorithm);
    }

    /**
     * In this function we generate the pair of keys using RSA and 4096 key lenght.
     * @return the pair of the keys
     * @throws Exception the exception if doesnt exist the RSA.
     */
    public  KeyPair generateKeyPair() throws Exception {
        this.generator.initialize(4096, new SecureRandom());

        return generator.generateKeyPair();
    }

    /**
     * This function converts the string into a public key.
     * @param pubKey the public key string as a parameter.
     * @return the publicKey.
     * @throws InvalidKeySpecException when there is an invalid key.
     */
    public PublicKey pubKeyStringToKey(String pubKey) throws InvalidKeySpecException {
        X509EncodedKeySpec converterSpec = new X509EncodedKeySpec(Base64.decodeBase64(pubKey));
        return keyFactory.generatePublic(converterSpec);
    }

    /**
     * This function converts the string into a private key.
     * @param privKey the private key string as a parameter.
     * @return the private key
     * @throws InvalidKeySpecException when there is an invalid key.
     */
    public PrivateKey privKeyStringToKey(String privKey) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec converterSpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privKey));
        return keyFactory.generatePrivate(converterSpec);
    }

    /**
     * The encryption function help us to encrypt the messages that we want.
     * @param plainText the message that we want to encrypt.
     * @param publicKey the key we are going to use to encrypt.
     * @return the ciphertext
     * @throws Exception exception.
     */
    public String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher encryptCipher = Cipher.getInstance(CryptoBox.cipherAlgorithm);
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

        return Base64.encodeBase64String(cipherText);
    }

    /**
     * The decryption function decrypts the ciphertext and converts it into a plaintext.
     * @param cipherText THe encrypted message that we want to decrypt.
     * @param privateKey The private key that we will use to decrypt.
     * @return The plaintext.
     * @throws Exception throws Exception.
     */
    public String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        byte[] bytes = Base64.decodeBase64(cipherText);

        Cipher decriptCipher = Cipher.getInstance(CryptoBox.cipherAlgorithm);
        decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(decriptCipher.doFinal(bytes), "UTF-8").trim();
    }

    /**
     * This function converts the Public key in to a string in order to store it or tranfer it
     * @param key the Pyblic key that we want to convert.
     * @return The key into a String format.
     */
    public String keyToString(PublicKey key) {
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * This function converts the Private key into a string in order to store it or transfer it.
     * @param key the Private Key that we want to convert.
     * @return the key into a String format.
     */
    public String keyToString(PrivateKey key) {
        return Base64.encodeBase64String(key.getEncoded());
    }
}
