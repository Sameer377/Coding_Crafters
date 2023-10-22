package com.agpitcodeclub.app.utils;

public class NotificationData {
    private String title;
    private String messege;

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
}
