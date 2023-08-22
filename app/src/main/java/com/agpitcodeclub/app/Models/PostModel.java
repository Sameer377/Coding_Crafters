package com.agpitcodeclub.app.Models;

import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class PostModel {

    public String title;
    public String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<SlideModel> getImgUri() {
        return imgUri;
    }

    public void setImgUri(ArrayList<SlideModel> imgUri) {
        this.imgUri = imgUri;
    }

    public ArrayList<SlideModel> imgUri;

    public PostModel() {
    }

    public PostModel(String title, String description) {
        this.title = title;
        this.description = description;
    }




    public PostModel(String title, String description,ArrayList<SlideModel> imgUri) {
        this.title = title;
        this.description = description;
        this.imgUri=imgUri;
    }


}
