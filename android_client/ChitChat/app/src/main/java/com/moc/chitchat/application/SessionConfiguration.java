package com.moc.chitchat.application;

import com.moc.chitchat.model.UserModel;

public class SessionConfiguration {

    /**
     * Current logged in user object
     */
    private UserModel currentUser;

    /**
     * getCurrentUser to get the current logged in user
     * @return the current user
     */
    public UserModel getCurrentUser() {
        return this.currentUser;
    }

    /**
     * setCurrentUser sets the current user
     * @param cUser the user object to set as current user
     */
    public void setCurrentUser(UserModel cUser) {
        this.currentUser = cUser;
    }

    /**
     * cleanCurrentUser to ensure there are no users logged in.
     */
    public void cleanCurrentUser() { this.currentUser = null; }
}
