package com.moc.chitchat;

/**
 * Created by aakyo on 23/01/2017.
 */

public class User {

    private String username;
    private String user_uid;
    private String password;

    public User() {}

    public User(String uN, String uid, String pW) {
        username = uN;
        user_uid = uid;
        password = pW;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public void setUsername(String newU) {username = newU;}

    public void setPassword(String newP) {password = newP;}
}
