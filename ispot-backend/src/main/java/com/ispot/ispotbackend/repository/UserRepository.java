package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
