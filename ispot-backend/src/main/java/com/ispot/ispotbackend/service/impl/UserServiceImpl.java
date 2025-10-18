package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.dto.UserDTO;
import com.ispot.ispotbackend.model.entity.User;
import com.ispot.ispotbackend.repository.UserRepository;
import com.ispot.ispotbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setNickname(userDTO.getNickname());
        user.setLat(userDTO.getLat() != null ? userDTO.getLat() : 0.0);
        user.setLng(userDTO.getLng() != null ? userDTO.getLng() : 0.0);
        user.setLastActiveTime(System.currentTimeMillis());

        userRepository.save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
        user.setLastActiveTime(System.currentTimeMillis());
        return user;
    }

    @Override
    public void updateLocation(String userId, double lat, double lng) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setLat(lat);
        user.setLng(lng);
        user.setLastActiveTime(System.currentTimeMillis());
        userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
