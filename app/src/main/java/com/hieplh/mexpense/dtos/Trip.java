package com.hieplh.mexpense.dtos;

public class Trip {
    private int id;
    private String tripName;
    private String destination;
    private String tripDate;
    private String description;
    private int riskAssessment;
    private String location;
    private String status;

    public Trip() {
    }

    public Trip(int id, String tripName, String destination, String tripDate, String description, int riskAssessment, String location, String status) {
        this.id = id;
        this.tripName = tripName;
        this.destination = destination;
        this.tripDate = tripDate;
        this.description = description;
        this.riskAssessment = riskAssessment;
        this.location = location;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(int riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "@!# " + tripName + "@!# " + destination + "@!# " + tripDate + "@!# " + description + "@!# "
                + riskAssessment + "@!# " + location + "@!# " + status;
    }

}
