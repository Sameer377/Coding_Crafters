package com.agpitcodeclub.app.credentials;

public class User {
    public String name,Email,password,username,profile,description,designation;
    public User(){

    }
    public User(String name,String Email,String password,String profile,String description,String designation){
        this.name=name;
        this.Email=Email;
        this.profile=profile;
        this.description=description;
        this.password=password;
        this.description=designation;
    }

    public User(String name,String Email,String password,String profile,String description){
        this.name=name;
        this.Email=Email;
        this.profile=profile;
        this.description=description;
        this.password=password;
    }
}

