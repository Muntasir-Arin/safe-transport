package com.safetransport.locationconsumerservice.dto;

public class VehicleLocation {
    private String vehicleId;
    private double latitude;
    private double longitude;

    public VehicleLocation(String vehicleId, double latitude, double longitude) {
        this.vehicleId = vehicleId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters & Setters
    public String getVehicleId() { return vehicleId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
