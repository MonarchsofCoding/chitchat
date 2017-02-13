package com.moc.chitchat.resolver;


import com.moc.chitchat.model.UserModel;

/**
 * UserResolver provides the methods involved with creating a User from parameters.
 */
public class UserResolver {

    /**
     * create returns a new User with the given parameters applied.
     * @param username the username for the User.
     * @param password the password for the User.
     * @param passwordCheck the password check for the User.
     * @return a new User with the given parameters.
     */
    public UserModel create(String username, String password, String passwordCheck) {
        UserModel user = new UserModel(username);

        user
            .setPassword(password)
            .setPasswordCheck(passwordCheck)
        ;

        return user;
    }
}
