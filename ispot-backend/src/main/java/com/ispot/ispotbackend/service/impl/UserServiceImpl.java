<<<<<<< HEAD
=======
/**
 * 用户业务层实现
 * ----------------------------
 * 1. 负责注册、登录、更新位置、查询用户列表等核心业务逻辑，
 *    所有数据库访问均委托给 UserRepository，保持单一职责。
 * 2. 当前异常统一使用 RuntimeException，后续可自定义 BizException
 *    配合 @ControllerAdvice 做全局异常映射，返回友好提示。
 * 3. 密码明文存储仅用于 demo，生产请使用 BCryptPasswordEncoder
 *    加密存储，并在登录时做 matches 校验。
 * 4. 位置字段（lat/lng）默认 0/0，调用端若无 GPS 需特殊处理；
 *    后续可接 GeoHash 或 PostGIS 做附近的人搜索。
 * 5. lastActiveTime 用于统计在线状态，可结合定时任务清理
 *    超过 N 分钟未更新的“幽灵”用户。
 *
 * 作者：your-name
 * 创建时间：2025-10-22
 */
>>>>>>> 7bc3476 (后端代码优化)
package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.dto.UserDTO;
import com.ispot.ispotbackend.model.entity.User;
import com.ispot.ispotbackend.repository.UserRepository;
import com.ispot.ispotbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(UserDTO userDTO) {
=======
/**
 * @Service 声明为业务层组件，Spring 会为其实现事务代理（如需事务可在方法上加 @Transactional）。
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 数据访问层依赖，由 Spring 自动注入。
     * 建议后续改为构造器注入，方便单元测试 mock。
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * 用户注册
     * 步骤：
     * 1) 校验用户名是否已存在
     * 2) DTO -> Entity 赋值，填充默认值
     * 3) 落库并返回完整实体（含生成的主键）
     *
     * @param userDTO 前端提交的用户信息，username/password/nickname 必填
     * @return 持久化后的 User 实体
     * @throws RuntimeException 用户名重复时抛出
     */
    @Override
    public User register(UserDTO userDTO) {
        // 快速失败：用户名唯一
>>>>>>> 7bc3476 (后端代码优化)
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
<<<<<<< HEAD
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setNickname(userDTO.getNickname());
=======
        user.setId(UUID.randomUUID().toString());          // 分布式主键
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());           // TODO: 生产环境需加密
        user.setNickname(userDTO.getNickname());
        // 如果前端没传坐标，默认 0/0（赤道/本初子午线交点，海面上）
>>>>>>> 7bc3476 (后端代码优化)
        user.setLat(userDTO.getLat() != null ? userDTO.getLat() : 0.0);
        user.setLng(userDTO.getLng() != null ? userDTO.getLng() : 0.0);
        user.setLastActiveTime(System.currentTimeMillis());

        userRepository.save(user);
        return user;
    }

<<<<<<< HEAD
    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        user.setLastActiveTime(System.currentTimeMillis());
        return user;
    }

=======
    /**
     * 登录
     * 步骤：
     * 1) 按用户名查询
     * 2) 密码匹配（明文比对，仅 demo）
     * 3) 更新最后活跃时间
     *
     * @param username 用户名
     * @param password 明文密码
     * @return 登录成功的用户实体
     * @throws RuntimeException 用户名或密码错误时抛出
     */
    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))  // TODO: 使用 BCrypt 匹配
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        // 每次登录都刷新活跃时间，可用于“在线”统计
        user.setLastActiveTime(System.currentTimeMillis());
        return user;  // 可由 Controller 写入 Session / JWT
    }

    /**
     * 更新用户实时位置
     * 适用于地图打卡、附近的人等功能。
     *
     * @param userId 用户主键
     * @param lat    纬度（-90 ~ 90）
     * @param lng    经度（-180 ~ 180）
     * @throws RuntimeException userId 不存在时抛出
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public void updateLocation(String userId, double lat, double lng) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setLat(lat);
        user.setLng(lng);
<<<<<<< HEAD
        user.setLastActiveTime(System.currentTimeMillis());
        userRepository.save(user);
    }

=======
        user.setLastActiveTime(System.currentTimeMillis()); // 顺带刷新活跃时间
        userRepository.save(user);
    }

    /**
     * 全量用户列表
     * 目前直接返回 Repository 结果，后续可加分页、关键字过滤、
     * 按 lastActiveTime 倒序等需求。
     *
     * @return 所有用户实体列表
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
