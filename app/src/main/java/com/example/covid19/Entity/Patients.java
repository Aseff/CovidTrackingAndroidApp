package com.example.covid19.Entity;

public class Patients {

    private  String name;
    private String mail;
    private String uid;
    private String type="patient";
    private String onlineStatus="online";
    private String typingTo="noOne";

    public Patients(){

    }
    public Patients(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }


    public Patients(String name, String mail,String type,String uid,String onlineStatus,String typingTo) {
        this.name = name;
        this.mail = mail;
        this.type=type;
        this.uid=uid;
        this.onlineStatus=onlineStatus;
        this.typingTo=typingTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }
}
