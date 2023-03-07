package com.example.trainingapp.Domain;

import java.time.LocalDateTime;

/**
 * Activity class, the domain class for the activities table in the database.
 *
 * @author David Sjöblom
 */
public class Activity {

    private Long id;
    private String name;
    private LocalDateTime date;
    private double distance;
    private double time;
    private ActivityType type;

    private String description;

    public Activity(Long id, String name, LocalDateTime date, double distance, double time, ActivityType type, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.distance = distance;
        this.time = time;
        this.type = type;
        this.description = description;
    }

    public Activity(String name, LocalDateTime date, double distance, double time, ActivityType type, String description) {
        this.name = name;
        this.date = date;
        this.distance = distance;
        this.time = time;
        this.type = type;
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date.toString();
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
