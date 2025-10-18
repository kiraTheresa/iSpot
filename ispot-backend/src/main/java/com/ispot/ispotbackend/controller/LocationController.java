package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.dto.PostDTO;
import com.ispot.ispotbackend.model.entity.LocationPoint;
import com.ispot.ispotbackend.service.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    // POST /api/location/update
    @PostMapping("/update")
    public PostDTO updateLocation(@RequestBody PostDTO body) {
        // 保存位置
        LocationPoint point = locationService.addLocation(body.getUserId(), body.getLat(), body.getLng());

        // 返回 PostDTO，只包含 userId、lat、lng
        PostDTO response = new PostDTO();
        response.setUserId(point.getUserId());
        response.setLat(point.getLat());
        response.setLng(point.getLng());
        return response;
    }

    // GET /api/location/all
    @GetMapping("/all")
    public List<PostDTO> getAllLocations() {
        return locationService.getAllLocations().stream()
                .map(p -> {
                    PostDTO dto = new PostDTO();
                    dto.setUserId(p.getUserId());
                    dto.setLat(p.getLat());
                    dto.setLng(p.getLng());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
