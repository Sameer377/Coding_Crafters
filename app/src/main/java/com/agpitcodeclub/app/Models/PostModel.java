package com.agpitcodeclub.app.Models;

public class PostModel {

    public String title;
    public String description;

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String imgUri;

    public PostModel() {
    }

    public PostModel(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
