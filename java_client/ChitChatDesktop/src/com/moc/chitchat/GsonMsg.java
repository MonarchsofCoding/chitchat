package com.moc.chitchat;

/**
 * Created by spiros on 21/01/17.
 */
public class GsonMsg {
    private String username;
    private String password;



    public GsonMsg(){}
    public GsonMsg(String username, String password){

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString()
    {
        return "usrpass [user=" + username +", pass=" + password+"]";
    }

}
