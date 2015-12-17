package com.example.li.eve_united.bean;



/**
 * Created by Li on 2015/11/12.
 */
public class ChatMessageBean {
    //DB_使用字段
    public static String TABLE_NAME="chat_msg_db";
    public static String COLUMN_MSG="msg";
    public static String COLUMN_NAME="name";
    public static String COLUMN_DATE="date";
    public static String COLUMN_TYPE="type";

    private String msg;
    private Type type;
    private String name;
    private String date;
    public enum Type
    {
        INCOMING, OUTCOMING
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
