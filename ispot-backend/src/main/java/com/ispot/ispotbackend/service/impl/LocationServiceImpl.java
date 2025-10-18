package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.LocationPoint;
import com.ispot.ispotbackend.repository.LocationRepository;
import com.ispot.ispotbackend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public LocationPoint addLocation(String userId, double lat, double lng) {
        LocationPoint point = new LocationPoint();
        point.setUserId(userId);
        point.setLat(lat);
        point.setLng(lng);

        locationRepository.save(point);
        return point;
    }

    @Override
    public List<LocationPoint> getAllLocations() {
        return locationRepository.findAll();
    }
}
