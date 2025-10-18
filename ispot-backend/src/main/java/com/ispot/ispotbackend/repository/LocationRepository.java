package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.LocationPoint;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class LocationRepository {
    private final Map<String, LocationPoint> locations = new HashMap<>();

    public void save(LocationPoint point) {
        locations.put(point.getUserId(), point);
    }

    public List<LocationPoint> findAll() {
        return new ArrayList<>(locations.values());
    }

    public List<LocationPoint> findByUserId(String userId) {
        List<LocationPoint> list = new ArrayList<>();
        for (LocationPoint p : locations.values()) {
            if (p.getUserId().equals(userId)) {
                list.add(p);
            }
        }
        return list;
    }
}
