package com.example.homework9_parta;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by KS Koumudi on 4/20/2017.
 */

public class Trip implements Serializable {
    private String title;
    private String owner;
    private double locationLat;
    private double locationLong;
    private String key;
    private ArrayList<String> members;

    public ArrayList<LocationAddress> getLocationAddresses() {
        return locationAddresses;
    }

    public void setLocationAddresses(ArrayList<LocationAddress> locationAddresses) {
        this.locationAddresses = locationAddresses;
    }

    private ArrayList<LocationAddress> locationAddresses;
    public Trip() {
        this.members=new ArrayList<String>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLong() {
        return locationLong;
    }

    public void setLocationLong(double locationLong) {
        this.locationLong = locationLong;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
