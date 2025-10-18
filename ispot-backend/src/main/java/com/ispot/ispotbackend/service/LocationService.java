package com.ispot.ispotbackend.service;

import com.ispot.ispotbackend.model.entity.LocationPoint;

import java.util.List;

public interface LocationService {
    LocationPoint addLocation(String userId, double lat, double lng);
    List<LocationPoint> getAllLocations();
}
