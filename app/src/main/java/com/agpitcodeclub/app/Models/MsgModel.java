package com.agpitcodeclub.app.Models;

public class MsgModel {
    public String title;

    public MsgModel(String title, String msg, String location, String time) {
        this.title = title;
        this.msg = msg;
        this.location = location;
        this.time = time;
    }

    public MsgModel() {
    }

    public String msg;

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String location;
    public String time;

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public MsgModel(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }
}
