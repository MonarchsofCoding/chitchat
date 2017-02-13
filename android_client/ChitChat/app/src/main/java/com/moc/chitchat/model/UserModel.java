package com.moc.chitchat.model;

import org.json.JSONObject;
import java.util.HashMap;

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

    /**
     * UserModel constructor
     * @param username the username of the User.
     */
    public UserModel(String username) {
        this.username = username;
    }

    /**
     * getUsername returns the username of the User.
     * @return the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * getPassword returns the password of the User.
     * @return the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword sets the password for the User.
     * @param password the new password.
     * @return the user.
     */
    public UserModel setPassword(String password) {
        this.password = password;

        return this;
    }

    /**
     * getPasswordCheck returns the check password of the User.
     * @return the password check of the user
     */
    public String getPasswordCheck() {
        return this.passwordCheck;
    }

    /**
     * setPasswordCheck sets the check password for the User.
     * @param passwordCheck the new check password.
     * @return the user.
     */
    public UserModel setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;

        return this;
    }

    /**
     * ToJsonObject returns a JSONObject representation of the User.
     * @return JSONObject representation of the User.
     */
    public JSONObject toJsonObject() {
        HashMap<String, String> userMap = new HashMap<>();

        userMap.put("username", this.getUsername());
        userMap.put("password", this.getPassword());

        return new JSONObject(userMap);
    }
}