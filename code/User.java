package com.example.homework9_parta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KS Koumudi on 4/18/2017.
 */

public class User implements Serializable {
    private String fname;
    private String lname;
    private String email;
    private String gender;
    private String uid;

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    private String photoURL;
    private ArrayList<String> requestSent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUid().equals(user.getUid());

    }

    @Override
    public int hashCode() {
        return getUid().hashCode();
    }

    public User() {
        this.requestRecieved=new ArrayList<String>();
        this.requestSent=new ArrayList<String>();
        this.friends=new ArrayList<String>();
    }

    public ArrayList<String> getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(ArrayList<String> requestSent) {
        this.requestSent = requestSent;
    }

    public ArrayList<String> getRequestRecieved() {
        return requestRecieved;
    }

    public void setRequestRecieved(ArrayList<String> requestRecieved) {
        this.requestRecieved = requestRecieved;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    private ArrayList<String> requestRecieved;
    private ArrayList<String> friends;

    @Override
    public String toString() {
        return "User{" +
                "fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", uid='" + uid + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", requestSent=" + requestSent +
                ", requestRecieved=" + requestRecieved +
                ", friends=" + friends +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
