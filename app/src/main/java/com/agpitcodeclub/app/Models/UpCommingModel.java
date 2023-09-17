package com.agpitcodeclub.app.Models;

public class UpCommingModel {
    public UpCommingModel() {
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    private String image;
    private String description;

    public UpCommingModel(String image, String description) {
        this.image = image;
        this.description = description;
    }
}