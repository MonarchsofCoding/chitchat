package com.moc.chitchat.resolver;

import com.moc.chitchat.model.UserModel;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * UserResolver provides the methods involved with converting parameters into a User object.
 */
@Component
public class UserResolver {

    /**
     * createUser returns a new User with the given parameters applied.
     *
     * @param username      String the username of the User.
     * @param password      String the password of the User.
     * @param passwordCheck String the password check of the User.
     * @return UserModel a new User with the given parameters.
     */
    public UserModel createUser(String username, String password, String passwordCheck) {
        UserModel user = new UserModel(username);

        user
                .setPassword(password)
                .setPasswordCheck(passwordCheck)
        ;

        return user;
    }

    /**
     * createUser returns a new User with the given parameters applied.
     *
     * @param username String the username of the User.
     * @return UserModel, a new User with the given parameter.
     */
    public UserModel createUser(String username) {
        return new UserModel(username);
    }

    /**
     * createUser returns a new User with the given parameters applied.
     * @param username - String the username of the User.
     * @param password - String the password of the User.
     * @return - UserModel a new User with the given parameter.
     */
    public UserModel createUser(String username, String password,PublicKey publickey,PrivateKey privatekey) {
        UserModel user = new UserModel(username);

        user.setPassword(password);
        user.setPrivatekey(privatekey);
        user.setPublicKey(publickey);

        return user;
    }

    /**
     * getUserModelViaJSonObject creates a new User by JSONObject.
     * @param jsonObject - object that has a key 'username'
     * @return - returns a new user
     */
    public UserModel getUserModelViaJSonObject(JSONObject jsonObject) {
        String username = jsonObject.getString("username");
        UserModel user = new UserModel(username);

        return user;
    }
}
