package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.dto.UserDTO;
import com.ispot.ispotbackend.model.dto.PostDTO;
import com.ispot.ispotbackend.model.entity.User;
import com.ispot.ispotbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
