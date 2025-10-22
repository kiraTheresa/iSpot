package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.Message;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD

import java.util.*;

@Repository
public class MessageRepository {
    private final Map<String, Message> messages = new HashMap<>();

=======
import java.util.*;

/**
 * 私信仓库（内存实现）
 * 正式环境请替换为 JPA / MyBatis / MongoDB 等持久化实现
 */
@Repository
public class MessageRepository {

    // 内存哈希表：messageId -> Message
    private final Map<String, Message> messages = new HashMap<>();

    /* -------------------- 基础 CRUD -------------------- */

    /**
     * 保存或更新私信（同一 ID 覆盖）
     */
>>>>>>> 7bc3476 (后端代码优化)
    public void save(Message message) {
        messages.put(message.getId(), message);
    }

<<<<<<< HEAD
=======
    /**
     * 获取全部私信（主要用于调试）
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

<<<<<<< HEAD
=======
    /* -------------------- 按业务维度查询 -------------------- */

    /**
     * 根据发送者 ID 查询所有发出的私信
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<Message> findBySenderId(String senderId) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            if (m.getSenderId().equals(senderId)) {
                list.add(m);
            }
        }
        return list;
    }

<<<<<<< HEAD
=======
    /**
     * 根据接收者 ID 查询所有收到的私信
     */
>>>>>>> 7bc3476 (后端代码优化)
    public List<Message> findByReceiverId(String receiverId) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            if (m.getReceiverId().equals(receiverId)) {
                list.add(m);
            }
        }
        return list;
    }

<<<<<<< HEAD
    public List<Message> findByUserPair(String userA, String userB) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            if ((m.getSenderId().equals(userA) && m.getReceiverId().equals(userB)) ||
                    (m.getSenderId().equals(userB) && m.getReceiverId().equals(userA))) {
                list.add(m);
            }
        }
        list.sort(Comparator.comparingLong(Message::getTimestamp));
        return list;
    }
}
=======
    /**
     * 查询两个用户之间的全部私信（双向）
     * 结果按时间戳升序排列，前端可直接渲染聊天流
     *
     * @param userA 用户A
     * @param userB 用户B
     * @return      已排序的私信列表
     */
    public List<Message> findByUserPair(String userA, String userB) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            boolean aToB = m.getSenderId().equals(userA) && m.getReceiverId().equals(userB);
            boolean bToA = m.getSenderId().equals(userB) && m.getReceiverId().equals(userA);
            if (aToB || bToA) {
                list.add(m);
            }
        }
        // 按时间升序，聊天流更直观
        list.sort(Comparator.comparingLong(Message::getTimestamp));
        return list;
    }
}
>>>>>>> 7bc3476 (后端代码优化)
