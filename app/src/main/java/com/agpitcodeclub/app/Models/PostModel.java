package com.agpitcodeclub.app.Models;

import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class PostModel {

    public String title;
    public String description;


    public String getPostuploadedon() {
        return postuploadedon;
    }

    public void setPostuploadedon(String postuploadedon) {
        this.postuploadedon = postuploadedon;
    }

    public String postuploadedon;
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

    public PostModel(String title, String description,String  postuploadedon) {
        this.title = title;
        this.description = description;
        this.postuploadedon=postuploadedon;
    }




    public PostModel(String title, String description,ArrayList<SlideModel> imgUri) {
        this.title = title;
        this.description = description;
        this.imgUri=imgUri;
    }
    public PostModel(String title, String description,ArrayList<SlideModel> imgUri,String postuploadedon) {
        this.title = title;
        this.description = description;
        this.imgUri=imgUri;
        this.postuploadedon=postuploadedon;
    }

    public String getImglist() {
        return imglist;
    }

    public void setImglist(String imglist) {
        this.imglist = imglist;
    }

    public String imglist;

    public PostModel(String title, String description,String imglist,String postuploadedon) {
        this.title = title;
        this.description = description;
        this.imglist=imglist;
        this.postuploadedon=postuploadedon;

    }


}
