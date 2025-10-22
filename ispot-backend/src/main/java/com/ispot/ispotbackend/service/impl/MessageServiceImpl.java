<<<<<<< HEAD
=======
/**
 * 消息业务层实现
 * ----------------------------
 * 1. 负责“发消息”与“查会话”两大核心场景，所有与repository打交道
 *    的细节都封装在本类，Controller 只负责参数校验与视图转换。
 * 2. 目前采用 UUID 全局唯一标识一条消息；时间戳取系统毫秒，
 *    如以后需要分布式、高并发，可换成雪花算法或数据库自增ID。
 * 3. 未加任何事务与限流，后续可补充：
 *    - @Transactional：保证“写消息+更新未读计数”原子性
 *    - 限流/防刷：基于令牌桶或 Redis 计数器
 *    - 消息签收：update read_status + WebSocket 推送
 *
 * 作者：your-name
 * 创建时间：2025-10-22
 */
>>>>>>> 7bc3476 (后端代码优化)
package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.Message;
import com.ispot.ispotbackend.repository.MessageRepository;
import com.ispot.ispotbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

<<<<<<< HEAD
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message sendMessage(String senderId, String receiverId, String content) {
        Message msg = new Message();
        msg.setId(UUID.randomUUID().toString());
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setTimestamp(System.currentTimeMillis());
        msg.setRead(false);

        messageRepository.save(msg);
        return msg;
    }

=======
/**
 * @Service 把当前类注册为业务层 Bean，Spring 会启用 AOP 代理，
 * 方便后续加事务、缓存、日志切面等。
 */
@Service
public class MessageServiceImpl implements MessageService {

    /**
     * 依赖注入，由 Spring 容器自动装配 MessageRepository 实例。
     * 建议构造器注入（RequiredArgsConstructor），此处为简洁保持字段注入。
     */
    @Autowired
    private MessageRepository messageRepository;

    /**
     * 发送一条点对点文本消息
     *
     * @param senderId   发送者主键，不可为 null
     * @param receiverId 接收者主键，不可为 null
     * @param content    消息正文，不可为 blank（建议 Controller 层先校验）
     * @return 持久化后的 Message 完整实体，包含生成的 id、时间戳、已读状态
     */
    @Override
    public Message sendMessage(String senderId, String receiverId, String content) {
        Message msg = new Message();
        msg.setId(UUID.randomUUID().toString());          // 全局唯一
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setContent(content);
        msg.setTimestamp(System.currentTimeMillis());     // 毫秒级
        msg.setRead(false);                               // 默认未读

        messageRepository.save(msg);                      // 落库
        return msg;                                       // 返回给上层做展示/推送
    }

    /**
     * 查询两个用户之间的双向聊天记录
     * 由 Repository 保证顺序（通常按 timestamp 升序）。
     *
     * @param userA 参与方 A
     * @param userB 参与方 B
     * @return 聊天记录列表，A→B 与 B→A 都会包含；不会返回 null
     */
>>>>>>> 7bc3476 (后端代码优化)
    @Override
    public List<Message> getMessagesBetween(String userA, String userB) {
        return messageRepository.findByUserPair(userA, userB);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 7bc3476 (后端代码优化)
