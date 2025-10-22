package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.dto.UserDTO;
<<<<<<< HEAD
import com.ispot.ispotbackend.model.dto.PostDTO;
=======
import com.ispot.ispotbackend.model.dto.LoginDTO;
>>>>>>> 7bc3476 (后端代码优化)
import com.ispot.ispotbackend.model.entity.User;
import com.ispot.ispotbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

=======
import java.util.List;
import java.util.Map;

/**
 * 用户模块对外暴露的 REST 接口
 * 统一前缀 /api/user
 */
@RestController                 // 告诉 Spring：这是控制器，所有返回值直接写成 JSON
@RequestMapping("/api/user")    // 类上统一加前缀，避免重复写
public class UserController {

    /* ========================= 依赖注入 ========================= */
    private final UserService userService;          // 业务逻辑全部交给 Service 层处理

    // 构造器注入（Spring 官方推荐方式，易测试、易 Mock）
>>>>>>> 7bc3476 (后端代码优化)
    public UserController(UserService userService) {
        this.userService = userService;
    }

<<<<<<< HEAD
    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> body) {
        return userService.login(body.get("username"), body.get("password"));
    }


    @GetMapping("/all")
    public List<User> allUsers() {
        return userService.getAllUsers();
    }
}
=======
    /* ========================= 接口方法 ========================= */

    /**
     * 用户注册
     * POST  http://localhost:8080/api/user/register
     * 请求体：{"username":"abc","password":"123","email":"xxx@xx.com", ...}
     * 成功返回：{"code":200,"data":{用户对象},"msg":null}
     * 失败返回：{"code":400,"data":null,"msg":"用户名已存在"}
     */
    @PostMapping("/register")
    public LoginDTO register(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.register(userDTO);   // 调用业务层真正的注册逻辑
            return LoginDTO.ok(user);               // 包装成统一返回对象
        } catch (RuntimeException e) {                   // 业务层抛出的所有运行时异常 => 统一转 400
            return LoginDTO.fail(e.getMessage());
        }
    }

    /**
     * 用户登录
     * POST  http://localhost:8080/api/user/login
     * 请求体：{"username":"abc","password":"123"}
     * 返回值格式同上
     */
    @PostMapping("/login")
    public LoginDTO login(@RequestBody Map<String, String> body) {
        try {
            // 直接从 Map 里拿字段，省去再建 DTO
            User user = userService.login(body.get("username"), body.get("password"));
            return LoginDTO.ok(user);
        } catch (RuntimeException e) {
            return LoginDTO.fail(e.getMessage());
        }
    }

    /**
     * 查询全部用户（演示用，实际生产要加权限 + 分页）
     * GET  http://localhost:8080/api/user/all
     */
    @GetMapping("/all")
    public List<User> allUsers() {
        return userService.getAllUsers();   // 直接返回 List，Spring 自动序列化成 JSON 数组
    }
}
>>>>>>> 7bc3476 (后端代码优化)
