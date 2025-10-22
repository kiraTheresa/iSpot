package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.User;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD

import java.util.*;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

=======
import java.util.*;

/**
 * 用户仓库（内存实现）
 * 正式环境请替换为 JPA / MyBatis / MongoDB 等持久化实现
 */
@Repository
public class UserRepository {

    // 内存哈希表：userId -> User
    private final Map<String, User> users = new HashMap<>();

    /* -------------------- 基础查询 -------------------- */

    /**
     * 根据主键 ID 查询用户
     * @return Optional 避免空指针
     */
>>>>>>> 7bc3476 (后端代码优化)
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

<<<<<<< HEAD
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
=======
    /**
     * 根据登录名查询用户（唯一索引）
     */
    public Optional<User> findByUsername(String username) {
        return users.values()
                .stream()
>>>>>>> 7bc3476 (后端代码优化)
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

<<<<<<< HEAD
=======
    /* -------------------- 增 / 全查 -------------------- */

    /**
     * 保存或更新用户（同一 ID 覆盖）
     */
>>>>>>> 7bc3476 (后端代码优化)
    public void save(User user) {
        users.put(user.getId(), user);
    }

<<<<<<< HEAD
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
=======
    /**
     * 获取全部用户（主要用于调试或后台管理）
     */
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
>>>>>>> 7bc3476 (后端代码优化)
