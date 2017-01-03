package com.example.q.myapplication;

/**
 * Created by q on 2017-01-03.
 */

public class ChatData {
    private String userName="";
    private String message="";
    private String time="";
    private String unique="";
    public ChatData() { }

    public ChatData(String userName, String message, String time, String unique) {
        this.userName = userName;
        this.message = message;
        this.time=time;
        this.unique=unique;
    }
    public String getUnique(){return unique;}

    public String getUserName() {
        return userName;
    }

    public String getTime(){
        return time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}