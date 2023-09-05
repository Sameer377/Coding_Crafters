package com.agpitcodeclub.app.Models;

public class User {
    public String name, email,password,username,profile,description,designation;
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
}

