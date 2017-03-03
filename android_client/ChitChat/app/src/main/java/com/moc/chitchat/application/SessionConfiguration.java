package com.moc.chitchat.application;

import android.support.v7.app.AppCompatActivity;

import com.moc.chitchat.model.UserModel;

public class SessionConfiguration {

    /* Current logged in user object
     */
    private UserModel currentUser;
    private AppCompatActivity currentChatActivity = null;

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
     * Returns if the current activity is the CurrentChat activity.
     * @return the CurrentChat AppCompatActivity object
     */
    public AppCompatActivity getCurrentChatActivity() { return currentChatActivity; }

    /**
     * Sets if the newActivity is a CurrentChat Activity.
     * @param newActivity
     */
    public void setCurrentChatActivity(AppCompatActivity newActivity) {
        if(newActivity.getComponentName().getClassName()
            .equals("com.moc.chitchat.activity.CurrentChatActivity")) {
            this.currentChatActivity = newActivity;
        }
    }

    /**
     * Clears the CurrentChat activity variable.
     */
    public void cleanCurrentChatActivity() { this.currentChatActivity = null; }

    /**
     * cleanCurrentUser to ensure there are no users logged in.
     */
    public void cleanCurrentUser() {
        this.currentUser = null;
    }

}
