package com.moc.chitchat.application;

import android.support.v7.app.AppCompatActivity;

import com.moc.chitchat.model.UserModel;

public class SessionConfiguration {

    /* Current logged in user object
     */
    private UserModel currentUser;

    /* Current Activity
     */
    private AppCompatActivity currentActivity;

    /* getCurrentUser to get the current logged in user
     * @return the current user
     */
    public UserModel getCurrentUser() {
        return this.currentUser;
    }

    /* setCurrentUser sets the current user
     * @param cUser the user object to set as current user
     */
    public void setCurrentUser(UserModel cuser) {
        this.currentUser = cuser;
    }

    /**
     * cleanCurrentUser to ensure there are no users logged in.
     */
    public void cleanCurrentUser() {
        this.currentUser = null;
    }

    /**
     * Return the current activity.
     * @return the current activity.
     */
    public AppCompatActivity getCurrentActivity() {
        return this.currentActivity;
    }

    public void setCurrentActivity(AppCompatActivity activity) {
        this.currentActivity = activity;
    }

}
