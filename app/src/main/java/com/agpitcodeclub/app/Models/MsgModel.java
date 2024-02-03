package com.agpitcodeclub.app.Models;

public class MsgModel {
    public String designation;
    public String date;

    public String getMsg_imguri() {
        return msg_imguri;
    }

    public String msg_imguri;
    public MsgModel(String designation, String msg, String profileimg,String msg_imguri,String date, String time) {
        this.designation = designation;
        this.msg = msg;
        this.profileimg = profileimg;
        this.time = time;
        this.date=date;
        this.msg_imguri=msg_imguri;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDate() {
        return date;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public MsgModel() {
    }

    public String msg;


    public String getTime() {
        return time;
    }

    public String profileimg;
    public String time;


    public String getMsg() {
        return msg;
    }

    public MsgModel(String designation, String msg) {
        this.designation = designation;
        this.msg = msg;
    }
}
