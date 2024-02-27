package com.agpitcodeclub.app.Models;

public class ConnectToMsgModel {

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String user;
    public String msg;
    public String date;

    public ConnectToMsgModel(String user, String msg, String date) {
        this.user = user;
        this.msg = msg;
        this.date = date;
    }

   public  ConnectToMsgModel(){}



}
