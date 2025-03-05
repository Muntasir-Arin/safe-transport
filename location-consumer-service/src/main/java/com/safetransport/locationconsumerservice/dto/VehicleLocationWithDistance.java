package com.safetransport.locationconsumerservice.dto;

public class VehicleLocationWithDistance {
    private String vehicleId;
    private double latitude;
    private double longitude;
    private double distance; // Distance from the current location in km

    public VehicleLocationWithDistance(String vehicleId, double latitude, double longitude, double distance) {
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    // Getters & Setters
    public String getVehicleId() { return vehicleId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
}
