package com.agpitcodeclub.app.Models;

public class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String name;
    public String email;
    public String password;
    public String username;
    public String profile;
    public String description;
    public String designation;

    public String getPersuing() {
        return persuing;
    }

    public void setPersuing(String persuing) {
        this.persuing = persuing;
    }

    public String persuing;
    public User(){

    }
    public User(String name,String Email,String password,String profile,String description,String designation){
        this.name=name;
        this.email =Email;
        this.profile=profile;
        this.description=description;
        this.password=password;
        this.description=designation;
    }

    public User(String name,String email,String password,String profile,String description){
        this.name=name;
        this.email =email;
        this.profile=profile;
        this.description=description;
        this.password=password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public User(String name, String email, String password,String persuing) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.persuing =persuing;
    }
}

