package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.Post;
import com.ispot.ispotbackend.repository.PostRepository;
import com.ispot.ispotbackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(String userId, String content, String imageUrl) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
        post.setUserId(userId);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setTimestamp(System.currentTimeMillis());
        post.setLikeCount(0);
        post.setCommentCount(0);

        postRepository.save(post);
        return post;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}
