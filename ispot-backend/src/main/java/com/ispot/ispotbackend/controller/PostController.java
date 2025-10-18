package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.entity.Post;
import com.ispot.ispotbackend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public Post createPost(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String content = body.get("content");
        String imageUrl = body.get("imageUrl");
        return postService.createPost(userId, content, imageUrl);
    }

    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
