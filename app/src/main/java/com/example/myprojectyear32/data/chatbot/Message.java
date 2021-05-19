package com.example.myprojectyear32.data.chatbot;
/**
 * Created by VMac on 17/11/16.
 */

import java.io.Serializable;

public class Message implements Serializable {
    String id, message, url;
    Type type;
    public Message() {
        this.type = Type.TEXT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public enum Type {
        TEXT,
        IMAGE
    }
}

