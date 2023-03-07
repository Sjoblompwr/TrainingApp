package com.example.trainingapp.Domain;


/**
 * ActivityLatLong class, the domain class for the activityLatLong table in the database.
 *
 * @author David Sjöblom
 */
public class ActivityLatLong {

    private double latitude;
    private double longitude;

    public ActivityLatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
