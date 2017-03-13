package com.moc.chitchat.model;

import android.util.Base64;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

import org.json.JSONObject;


/**
 * UserModel provides the representation of a User entity.
 */
public class UserModel {

    /**
     * username of the User.
     */
    private String username;

    /**
     * password of the User.
     */
    private String password;

    /**
     * passwordCheck of the user to be used during local validation.
     */
    private String passwordCheck;

    /* authToken of the user to authenticate while doing requests to the server.
     */
    private String authToken;

    /**
     * Private KEy of the user.
     */
    private PrivateKey privateKey;

    /**
     * Public key of the user.
     */
    private PublicKey publicKey;

    /** CryptoBox.
     * TODO: returns null from @Inject.
     */
    //@Inject CryptoBox cryptoBox;

    /**
     * UserModel constructor
     *
     * @param username the username of the User.
     */
    public UserModel(String username) {
        this.username = username;
    }

    /**
     * getUsername returns the username of the User.
     *
     * @return the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * getPassword returns the password of the User.
     *
     * @return the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword sets the password for the User.
     *
     * @param password the new password.
     * @return the user.
     */
    public UserModel setPassword(String password) {
        this.password = password;

        return this;
    }

    /**
     * getPasswordCheck returns the check password of the User.
     *
     * @return the password check of the user.
     */
    public String getPasswordCheck() {
        return this.passwordCheck;
    }

    /**
     * setPasswordCheck sets the check password for the User.
     *
     * @param passwordCheck the new check password.
     * @return the user.
     */
    public UserModel setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;

        return this;
    }

    /* getAuthToken returns the authToken.
     * @return the token.
     */
    public String getAuthToken() {
        return this.authToken;
    }

    /**
     * setAuthToken sets an auth token to the user.
     *
     * @param aaToken the token to be set.
     * @return the user.
     */
    public UserModel setAuthToken(String aaToken) {
        this.authToken = aaToken;

        return this;
    }

    /**
     * Return the private key of the user.
     * @return the private key.
     */
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    /**
     * Sets the new private key to user model.
     * @param key the new private key.
     */
    public UserModel setPrivateKey(PrivateKey key) {
        this.privateKey = key;

        return this;
    }


    /**
     * Return the public key of the user.
     * @return the public key.
     */
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /**
     * Sets the new public key to user model.
     * @param key the enw public key.
     */
    public UserModel setPublicKey(PublicKey key) {
        this.publicKey = key;

        return this;
    }

    /**
     * To return the names on ListView while providing the whole data inside.
     * @return The username of the user to display on the ListView.
     */
    @Override
    public String toString() {
        return this.getUsername();
    }

    /**
     * ToJsonObject returns a JSONObject representation of the User for registration process.
     *
     * @return JSONObject representation of the User.
     */
    public JSONObject toJsonObjectForRegister() {
        HashMap<String, String> userMap = new HashMap<>();

        userMap.put("username", this.getUsername());
        userMap.put("password", this.getPassword());

        return new JSONObject(userMap);
    }

    /**
     * ToJsonObject returns a JSONObject representation of the User for login process.
     * @return JSONObject representation of the User.
     */
    public JSONObject toJsonObjectForLogin() {
        HashMap<String, String> userMap = new HashMap<>();

        userMap.put("username", this.getUsername());
        userMap.put("password", this.getPassword());
        userMap.put(
            "public_key",
            Base64.encodeToString(this.getPublicKey().getEncoded(),Base64.DEFAULT)
        );

        return new JSONObject(userMap);
    }
}
