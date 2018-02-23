package com.example.homework9_parta;

import java.io.Serializable;

/**
 * Created by praka on 4/28/2017.
 */

public class LocationAddress implements Serializable{
   private double locationLat,locationLong;


    private String adddressString;
    public double getLocationLat() {
        return locationLat;
    }
    public String getAdddressString() {
        return adddressString;
    }

    public void setAdddressString(String adddressString) {
        this.adddressString = adddressString;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationAddress that = (LocationAddress) o;

        if (Double.compare(that.locationLat, locationLat) != 0) return false;
        return Double.compare(that.locationLong, locationLong) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(locationLat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(locationLong);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "LocationAddress{" +
                "locationLat=" + locationLat +
                ", locationLong=" + locationLong +
                ", adddressString='" + adddressString + '\'' +
                '}';
    }
}
