package com.ispot.ispotbackend.service;

import com.ispot.ispotbackend.model.entity.Post;

import java.util.List;

public interface PostService {
    Post createPost(String userId, String content, String imageUrl);
    List<Post> getAllPosts();
}
