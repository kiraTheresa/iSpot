package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.entity.Post;
import com.ispot.ispotbackend.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

<<<<<<< HEAD
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

=======
/**
 * 帖子（Post）模块 REST 接口
 * 统一前缀 /api/post
 */
@RestController               // 声明为 SpringMVC 控制器，返回 JSON
@RequestMapping("/api/post")  // 类级别路径前缀
public class PostController {

    /* ==================== 依赖注入 ==================== */
    private final PostService postService;

    // 构造器注入：方便单元测试，Spring 自动装配
>>>>>>> 7bc3476 (后端代码优化)
    public PostController(PostService postService) {
        this.postService = postService;
    }

<<<<<<< HEAD
    @PostMapping("/create")
    public Post createPost(@RequestBody Map<String, String> body) {
        String userId = body.get("userId");
        String content = body.get("content");
=======
    /**
     * 创建帖子
     * POST  http://localhost:8080/api/post/create
     * 请求体 JSON 示例：
     * {
     *   "userId": "1",
     *   "content": "今天天气真好",
     *   "imageUrl": "https://xxx.jpg"   // 可空
     * }
     * 返回：刚创建的 Post 实体（含 id、createTime 等）
     */
    @PostMapping("/create")
    public Post createPost(@RequestBody Map<String, String> body) {
        // 简单取字段；如果字段多建议用 DTO 接收，方便校验
        String userId   = body.get("userId");
        String content  = body.get("content");
>>>>>>> 7bc3476 (后端代码优化)
        String imageUrl = body.get("imageUrl");
        return postService.createPost(userId, content, imageUrl);
    }

<<<<<<< HEAD
=======
    /**
     * 查询全部帖子（演示用，生产环境请加分页 + 权限）
     * GET  http://localhost:8080/api/post/all
     * 返回：Post 实体列表，Spring 自动序列化成 JSON 数组
     */
>>>>>>> 7bc3476 (后端代码优化)
    @GetMapping("/all")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
