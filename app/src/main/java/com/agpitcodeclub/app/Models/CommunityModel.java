package com.agpitcodeclub.app.Models;


public class CommunityModel {
    private String name;
    private String email;
    private String password;
    private String username;
    private String profile;
    private String description;
    private String persuing;

    public String getPersuing() {
        return persuing;
    }

    public void setPersuing(String persuing) {
        this.persuing = persuing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String designation;
    public CommunityModel(){

    }


    public CommunityModel(String name, String email, String password, String username, String profile, String description, String persuing, String designation, String userid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.username = username;
        this.profile = profile;
        this.description = description;
        this.persuing = persuing;
        this.designation = designation;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String userid;

    public CommunityModel(String name, String email, String password, String profile, String persuing, String description, String designation){
        this.name=name;
        this.email=email;
        this.profile=profile;
        this.description=description;
        this.password=password;
        this.description=designation;
        this.persuing=persuing;
    }

    public CommunityModel(String name, String email, String password, String profile, String persuing, String description){
        this.persuing=persuing;
        this.name=name;
        this.email=email;
        this.profile=profile;
        this.description=description;
        this.password=password;
    }
}
