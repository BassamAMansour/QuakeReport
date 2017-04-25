package com.example.android.quakereport;

/**
 * Created by Bassam on 2/22/2017.
 */

public class Earthquake {

    private double magnitude;
    private long timeUNIX;
    private String location;
    private String USGS_URL;

    public Earthquake(String location, long timeUNIX, double magnitude, String USGS_URL) {
        this.magnitude = magnitude;
        this.timeUNIX = timeUNIX;
        this.location = location;
        this.USGS_URL = USGS_URL;

    }

    public String getUSGS_URL() {
        return USGS_URL;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTimeUNIX() {
        return timeUNIX;
    }
}
