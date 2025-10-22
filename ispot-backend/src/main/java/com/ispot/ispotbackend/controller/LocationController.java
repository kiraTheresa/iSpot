package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.dto.PostDTO;
import com.ispot.ispotbackend.model.entity.LocationPoint;
import com.ispot.ispotbackend.service.LocationService;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
public class LocationController {

=======
import java.util.List;
import java.util.stream.Collectors;

/**
 * 位置（Location）模块 REST 接口
 * 统一前缀 /api/location
 * 允许前端 localhost:8000 跨域访问
 */
@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "http://localhost:8000")
public class LocationController {

    /* ==================== 依赖注入 ==================== */
>>>>>>> 7bc3476 (后端代码优化)
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

<<<<<<< HEAD
    // POST /api/location/update
    @PostMapping("/update")
    public PostDTO updateLocation(@RequestBody PostDTO body) {
        // 保存位置
        LocationPoint point = locationService.addLocation(body.getUserId(), body.getLat(), body.getLng());

        // 返回 PostDTO，只包含 userId、lat、lng
=======
    /**
     * 更新或新增用户位置
     * POST  http://localhost:8080/api/location/update
     * 请求体（PostDTO 仅用作传输，字段示例）：
     * {
     *   "userId": "1",
     *   "lat": 39.9042,
     *   "lng": 116.4074
     * }
     * 返回：同样是 PostDTO，带上刚刚保存的坐标
     */
    @PostMapping("/update")
    public PostDTO updateLocation(@RequestBody PostDTO body) {
        // 1. 调用 Service 保存/更新位置
        LocationPoint point = locationService.addLocation(
                body.getUserId(), body.getLat(), body.getLng());

        // 2. 把实体再转回 DTO 返回给前端
>>>>>>> 7bc3476 (后端代码优化)
        PostDTO response = new PostDTO();
        response.setUserId(point.getUserId());
        response.setLat(point.getLat());
        response.setLng(point.getLng());
        return response;
    }

<<<<<<< HEAD
    // GET /api/location/all
    @GetMapping("/all")
    public List<PostDTO> getAllLocations() {
        return locationService.getAllLocations().stream()
=======
    /**
     * 获取所有用户最新位置列表
     * GET  http://localhost:8080/api/location/all
     * 返回：List<PostDTO>，Spring 自动序列化成 JSON 数组
     */
    @GetMapping("/all")
    public List<PostDTO> getAllLocations() {
        // 1. 查询全部 LocationPoint 实体
        // 2. Java 8 Stream 把实体列表转成 DTO 列表
        return locationService.getAllLocations()
                .stream()
>>>>>>> 7bc3476 (后端代码优化)
                .map(p -> {
                    PostDTO dto = new PostDTO();
                    dto.setUserId(p.getUserId());
                    dto.setLat(p.getLat());
                    dto.setLng(p.getLng());
                    return dto;
                })
                .collect(Collectors.toList());
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
