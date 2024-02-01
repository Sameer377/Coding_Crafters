package com.agpitcodeclub.app.utils;


import java.net.URL;

public class NotificationData {
    private String title;
    private String messege;
    private URL imgURL=null;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public NotificationData(String title, String messege) {
        this.title = title;
        this.messege = messege;
    }


    public NotificationData(String title, String messege, URL imgURL) {
        this.title = title;
        this.messege = messege;
        this.imgURL = imgURL;
    }

    public URL getImgURL() {
        return imgURL;
    }
}
