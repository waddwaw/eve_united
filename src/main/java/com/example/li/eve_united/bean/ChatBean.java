package com.example.li.eve_united.bean;

import java.io.Serializable;

/**
 * Created by Li on 2015/11/10.
 */
public class ChatBean implements Serializable{
    private  String user;
    private  String type ;
    private  String content;

    public ChatBean(String type, String user, String content) {
        this.type = type;
        this.user = user;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
