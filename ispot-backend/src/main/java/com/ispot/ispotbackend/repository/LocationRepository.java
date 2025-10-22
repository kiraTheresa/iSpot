package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.LocationPoint;
import org.springframework.stereotype.Repository;

import java.util.*;

<<<<<<< HEAD
@Repository
public class LocationRepository {
    private final Map<String, LocationPoint> locations = new HashMap<>();

=======
/**
 * 位置数据仓库（内存实现）
 * 正式环境请替换为 JPA / MyBatis / MongoDB 等持久化实现
 */
@Repository
public class LocationRepository {

    // 内存哈希表：userId -> 最新位置（保证每个用户只存一条）
    private final Map<String, LocationPoint> locations = new HashMap<>();

    /**
     * 保存或更新用户位置
     * 同一个 userId 重复调用会覆盖旧值
     */
>>>>>>> 7bc3476 (后端代码优化)
    public void save(LocationPoint point) {
        locations.put(point.getUserId(), point);
    }

<<<<<<< HEAD
=======
    /**
     * 查询全部用户的最新位置
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<LocationPoint> findAll() {
        return new ArrayList<>(locations.values());
    }

<<<<<<< HEAD
=======
    /**
     * 根据用户ID查询其所有位置记录（本实现只有最新一条）
     * 若后续扩展为“历史轨迹”，可返回 List
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<LocationPoint> findByUserId(String userId) {
        List<LocationPoint> list = new ArrayList<>();
        for (LocationPoint p : locations.values()) {
            if (p.getUserId().equals(userId)) {
                list.add(p);
            }
        }
        return list;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
