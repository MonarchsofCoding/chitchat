package com.moc.chitchat;

/**
 * Created by spiros on 22/01/17.
 */
public class Messages {
private String name, message;

public Messages(){}
public Messages(String name, String message ){
    this.name= name;
    this.message=message;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
