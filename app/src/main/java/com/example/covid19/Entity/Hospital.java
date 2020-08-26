package com.example.covid19.Entity;

public class Hospital {
    private String name, email, password,url;
    private String type="hospital";


    public Hospital(){

    }
    public Hospital(String name, String email, String url) {
        this.name = name;
        this.email = email;
        this.url = url;
    }


    public Hospital(String name, String email, String url,String type) {
        this.name = name;
        this.email = email;
        this.url = url;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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

}
