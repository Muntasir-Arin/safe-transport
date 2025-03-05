package com.safetransport.locationconsumerservice.service;

import com.safetransport.locationconsumerservice.dto.VehicleLocation;
import com.safetransport.locationconsumerservice.dto.VehicleLocationWithDistance;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VehicleLocationService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String VEHICLE_GEO_KEY = "vehicle_locations";

    public VehicleLocationService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void updateVehicleLocation(String vehicleId, double latitude, double longitude) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        geoOps.add(VEHICLE_GEO_KEY, new Point(longitude, latitude), vehicleId);
    }


    public List<String> getAllVehicles() {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        Set<String> vehicleIds = redisTemplate.opsForZSet().range(VEHICLE_GEO_KEY, 0, -1);
        if (vehicleIds == null || vehicleIds.isEmpty()) {
            return List.of();
        }
        return List.copyOf(vehicleIds);
    }


    public List<VehicleLocation> getNearbyVehicles(double latitude, double longitude, double radiusInKm) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        Point center = new Point(longitude, latitude);
        Distance radius = new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS);

        Boolean exists = redisTemplate.hasKey(VEHICLE_GEO_KEY);
        if (!exists) {
            System.err.println("⚠ Error: Geo key " + VEHICLE_GEO_KEY + " does not exist in Redis.");
            return List.of();
        }

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(VEHICLE_GEO_KEY, new Circle(center, radius));

        if (results == null || results.getContent().isEmpty()) {
            System.err.println("⚠ Warning: No vehicles found within the radius.");
            return List.of();
        }

        return results.getContent().stream()
                .map(result -> {
                    String vehicleId = result.getContent().getName();

                    return getVehicleLocation(geoOps, vehicleId);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }



    public List<VehicleLocation> getAllVehiclesWithLocations() {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();

        Set<String> vehicleIds = redisTemplate.opsForZSet().range(VEHICLE_GEO_KEY, 0, -1);

        if (vehicleIds == null || vehicleIds.isEmpty()) {
            System.err.println("⚠ Warning: No vehicles found in Redis.");
            return List.of();
        }

        return vehicleIds.stream()
                .map(vehicleId -> {
                    return getVehicleLocation(geoOps, vehicleId);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private VehicleLocation getVehicleLocation(GeoOperations<String, String> geoOps, String vehicleId) {
        List<Point> points = geoOps.position(VEHICLE_GEO_KEY, vehicleId);
        if (points != null && !points.isEmpty()) {
            Point point = points.get(0);
            return new VehicleLocation(vehicleId, point.getY(), point.getX());
        }
        System.err.println("⚠ Warning: Vehicle " + vehicleId + " has no location data in Redis.");
        return null;
    }

    public List<VehicleLocationWithDistance> getNearbyVehiclesWithDistance(double latitude, double longitude, double radiusInKm) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        Point center = new Point(longitude, latitude);
        Distance radius = new Distance(radiusInKm, RedisGeoCommands.DistanceUnit.KILOMETERS);

        Boolean exists = redisTemplate.hasKey(VEHICLE_GEO_KEY);
        if (!exists) {
            System.err.println("⚠ Error: Geo key " + VEHICLE_GEO_KEY + " does not exist in Redis.");
            return List.of();
        }

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(VEHICLE_GEO_KEY, new Circle(center, radius),
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance());

        if (results == null || results.getContent().isEmpty()) {
            System.err.println("⚠ Warning: No vehicles found within the radius.");
            return List.of();
        }

        return results.getContent().stream()
                .map(result -> {
                    String vehicleId = result.getContent().getName();
                    double distance = result.getDistance().getValue(); // Distance in km

                    VehicleLocation vehicleLocation = getVehicleLocation(geoOps, vehicleId);
                    if (vehicleLocation != null) {
                        return new VehicleLocationWithDistance(
                                vehicleLocation.getVehicleId(),
                                vehicleLocation.getLatitude(),
                                vehicleLocation.getLongitude(),
                                distance
                        );
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

}