package com.moc.chitchat;

import org.json.JSONObject;

/**
 * Created by aakyo on 23/01/2017.
 */

public class UserModel {

    private String username;
    //TODO Aydin: put uID here after establishing how to use UID on connections
    private String password;
    private String passwordRe;

    public UserModel() {}

    public UserModel(String uN,/* String uid,*/ String pW, String pRW) {
        username = uN;
        //user_uid = uid;
        password = pW;
        passwordRe = pRW;
    }

    public String getUsername() {return username;}

    public String getPassword() {return password;}

    public String getRePassword() {return passwordRe;}

    public void setUsername(String newU) {username = newU;}

    public void setPassword(String newP) {password = newP;}

    public void setRePassword(String newRP) {passwordRe = newRP;}

    public JSONObject getJSON() throws Exception{
        return new JSONObject()
        .put("username",username)
        .put("password",password);
    }
}
