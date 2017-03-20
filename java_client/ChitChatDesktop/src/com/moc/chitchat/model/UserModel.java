package com.moc.chitchat.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.json.JSONObject;
import org.json.JSONString;


/**
 * UserModel provides the representation of a User entity.
 */
public class UserModel implements JSONString {

    /**
     * username of the User.
     */
    private String username;

    /**
     * password of the User.
     */
    private String password;

    /**
     * passwordCheck of the User to be used during local validator.
     */
    private String passwordCheck;

    /**
     * authToken in order to check the User authenticity.
     */
    private String authToken;

    /**
     * The Private Key of the user.
     */
    private PrivateKey privatekey;

    /**
     * Returns the private key.
     *
     * @return the private key.
     */
    public PrivateKey getPrivatekey() {
        return privatekey;
    }

    /**
     * Sets the private key of the user.
     *
     * @param privatekey the private key.
     */
    public void setPrivatekey(PrivateKey privatekey) {
        this.privatekey = privatekey;
    }

    /**
     * Returns the public key of the user.
     *
     * @return the public key.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the public key of the user.
     *
     * @param publicKey the public key.
     */
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * The publicKey of the user.
     */
    private PublicKey publicKey;

    /**
     * UserModel Constructor.
     *
     * @param username String The username of the User.
     */
    public UserModel(String username) {
        this.username = username;
    }

    /**
     * getUsername returns the username of the User.
     *
     * @return String the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * getPassword returns the password of the User.
     *
     * @return String the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword sets the password of the User.
     *
     * @param password String the new password.
     * @return UserModel the User that was mutated.
     */
    public UserModel setPassword(String password) {
        this.password = password;

        return this;
    }

    /**
     * getPasswordCheck returns the passwordCheck of the User.
     *
     * @return String the password check.
     */
    public String getPasswordCheck() {
        return this.passwordCheck;
    }

    /**
     * setPasswordCheck sets the check password of the User.
     *
     * @param passwordCheck String the new check password.
     * @return UserModel the User that was mutated.
     */
    public UserModel setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;

        return this;
    }

    /**
     * toJSONString returns the JSON representation of this UserModel.
     *
     * @return String JSON representation of the UserModel.
     */
    @Override
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        if (this.getPublicKey() != null) {

            jsonObject
                    .put("username", this.username)
                    .put("password", this.password)
                    .put("public_key", Base64.getEncoder().encodeToString(this.getPublicKey().getEncoded()));
        } else {
            jsonObject
                    .put("username", this.username)
                    .put("password", this.password)
            ;
        }

        return jsonObject.toString();
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public String toString() {
        return this.username;
    }
}
