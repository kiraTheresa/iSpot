package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.Post;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD

import java.util.*;

@Repository
public class PostRepository {
    private final Map<String, Post> posts = new HashMap<>();

=======
import java.util.*;

/**
 * 帖子仓库（内存实现）
 * 正式环境请替换为 JPA / MyBatis / MongoDB 等持久化实现
 */
@Repository
public class PostRepository {

    // 内存哈希表：postId -> Post
    private final Map<String, Post> posts = new HashMap<>();

    /**
     * 保存或更新帖子（同一 ID 覆盖）
     */
>>>>>>> 7bc3476 (后端代码优化)
    public void save(Post post) {
        posts.put(post.getId(), post);
    }

<<<<<<< HEAD
=======
    /**
     * 查询全部帖子（默认顺序：按自然插入顺序）
     * 若需要倒序/分页，可在 Service 层处理
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

<<<<<<< HEAD
=======
    /**
     * 根据用户 ID 查询其发布的所有帖子
     * 返回顺序：自然插入顺序；如需按时间倒序可在 SQL 或 Service 里排序
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<Post> findByUserId(String userId) {
        List<Post> list = new ArrayList<>();
        for (Post p : posts.values()) {
            if (p.getUserId().equals(userId)) {
                list.add(p);
            }
        }
        return list;
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
