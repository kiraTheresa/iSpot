package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.LocationPoint;
import com.ispot.ispotbackend.repository.LocationRepository;
import com.ispot.ispotbackend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
<<<<<<< HEAD
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

=======

/**
 * 位置业务逻辑实现
 * 当前为内存实现，后续可无缝切换为数据库持久化
 */
@Service
public class LocationServiceImpl implements LocationService {

    /* ==================== 数据层依赖 ==================== */
    @Autowired
    private LocationRepository locationRepository;

    /**
     * 新增或覆盖用户最新位置
     * 1. 组装 LocationPoint 实体（ID 由仓库生成或雪花算法）
     * 2. 调用仓库 save：同一 userId 重复插入会覆盖旧值
     *
     * @param userId 用户唯一标识
     * @param lat    纬度（WGS84）
     * @param lng    经度（WGS84）
     * @return       保存后的 LocationPoint（含时间戳、主键等）
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public LocationPoint addLocation(String userId, double lat, double lng) {
        LocationPoint point = new LocationPoint();
        point.setUserId(userId);
        point.setLat(lat);
        point.setLng(lng);
<<<<<<< HEAD

=======
        // 若实体需要 ID，可在构造时生成，或仓库 save 方法内部赋值
>>>>>>> 7bc3476 (后端代码优化)
        locationRepository.save(point);
        return point;
    }

<<<<<<< HEAD
=======
    /**
     * 获取所有用户最新位置列表
     * 当前返回内存快照；生产环境可接分页、按更新时间倒序
     *
     * @return 全部 LocationPoint 列表
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public List<LocationPoint> getAllLocations() {
        return locationRepository.findAll();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
