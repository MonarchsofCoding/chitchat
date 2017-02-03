package com.moc.chitchat.model;


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
     * UserModel Constructor.
     * @param username String The username of the User.
     */
    public UserModel(String username) {
        this.username = username;
    }

    /**
     * getUsername returns the username of the User.
     * @return String the username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * getPassword returns the password of the User.
     * @return String the password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword sets the password of the User.
     * @param password String the new password.
     * @return UserModel the User that was mutated.
     */
    public UserModel setPassword(String password) {
        this.password = password;

        return this;
    }

    /**
     * getPasswordCheck returns the passwordCheck of the User.
     * @return String the password check.
     */
    public String getPasswordCheck() {
        return this.passwordCheck;
    }

    /**
     * setPasswordCheck sets the check password of the User.
     * @param passwordCheck String the new check password.
     * @return UserModel the User that was mutated.
     */
    public UserModel setPasswordCheck(String passwordCheck) {
        this.passwordCheck = passwordCheck;

        return this;
    }

    /**
     * toJSONString returns the JSON representation of this UserModel.
     * @return String JSON representation of the UserModel.
     */
    @Override
    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject
            .put("username", this.username)
            .put("password", this.password)
        ;

        return jsonObject.toString();
    }
}
