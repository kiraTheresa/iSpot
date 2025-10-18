package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {
    private final Map<String, Post> posts = new HashMap<>();

    public void save(Post post) {
        posts.put(post.getId(), post);
    }

    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    public List<Post> findByUserId(String userId) {
        List<Post> list = new ArrayList<>();
        for (Post p : posts.values()) {
            if (p.getUserId().equals(userId)) {
                list.add(p);
            }
        }
        return list;
    }
}
