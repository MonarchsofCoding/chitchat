package com.moc.chitchat.resolver;

import com.moc.chitchat.model.UserModel;
import org.springframework.stereotype.Component;

/**
 * UserResolver provides the methods involved with converting parameters into a User object.
 */
@Component
public class UserResolver {

    /**
     * createUser returns a new User with the given parameters applied.
     * @param username String the username of the User.
     * @param password String the password of the User.
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
     * @param username String the username of the User.
     * @return UserModel a new User with the given parameter.
     */
    public UserModel createUser(String username) {
        return new UserModel(username);
    }
}
