package com.safetransport.locationconsumerservice.controller;

import com.safetransport.locationconsumerservice.dto.VehicleLocation;
import com.safetransport.locationconsumerservice.service.VehicleLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleLocationService vehicleService;

    public VehicleController(VehicleLocationService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/update")
    public String updateVehicle(@RequestParam String vehicleId, @RequestParam double lat, @RequestParam double lon) {
        vehicleService.updateVehicleLocation(vehicleId, lat, lon);
        return "Updated location for vehicle: " + vehicleId;
    }

    @GetMapping("/all")
    public List<String> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/all/locations")
    public List<VehicleLocation> getAllVehicleLocations() {
        return vehicleService.getAllVehiclesWithLocations();
    }

    @GetMapping("/nearby")
    public List<VehicleLocation> getNearbyVehicles(@RequestParam double lat, @RequestParam double lon, @RequestParam double radius) {
        return vehicleService.getNearbyVehicles(lat, lon, radius);
    }
}
