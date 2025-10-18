package com.ispot.ispotbackend.service;

import com.ispot.ispotbackend.model.dto.UserDTO;
import com.ispot.ispotbackend.model.entity.User;

import java.util.List;

public interface UserService {
    User register(UserDTO userDTO);
    User login(String username, String password);
    void updateLocation(String userId, double lat, double lng);
    List<User> getAllUsers();
}
