package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.Post;
import com.ispot.ispotbackend.repository.PostRepository;
import com.ispot.ispotbackend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(String userId, String content, String imageUrl) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());
=======
/**
 * 帖子业务逻辑实现
 * Spring 会自动扫描 @Service 并注入接口
 */
@Service
public class PostServiceImpl implements PostService {

    /* ==================== 数据层依赖 ==================== */
    @Autowired
    private PostRepository postRepository;

    /**
     * 创建并保存一条新帖子
     * 1. 组装 Post 实体，生成全局唯一 ID
     * 2. 初始化点赞数、评论数为 0
     * 3. 保存到内存仓库（或数据库）
     *
     * @param userId   发帖用户ID
     * @param content  帖子正文
     * @param imageUrl 配图地址，可空
     * @return         保存后的 Post 实体（含主键、时间戳等）
     */
    @Override
    public Post createPost(String userId, String content, String imageUrl) {
        Post post = new Post();
        post.setId(UUID.randomUUID().toString());   // 分布式场景可换成雪花算法
>>>>>>> 7bc3476 (后端代码优化)
        post.setUserId(userId);
        post.setContent(content);
        post.setImageUrl(imageUrl);
        post.setTimestamp(System.currentTimeMillis());
<<<<<<< HEAD
        post.setLikeCount(0);
        post.setCommentCount(0);

        postRepository.save(post);
        return post;
    }

=======
        post.setLikeCount(0);      // 初始点赞数
        post.setCommentCount(0);   // 初始评论数

        postRepository.save(post); // 落地
        return post;               // 返回完整对象
    }

    /**
     * 查询全部帖子
     * 当前实现直接返回内存列表；生产环境建议加分页、按时间倒序
     *
     * @return 帖子列表（自然插入顺序）
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
