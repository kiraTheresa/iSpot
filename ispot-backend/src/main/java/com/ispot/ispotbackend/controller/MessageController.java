package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.entity.Message;
import com.ispot.ispotbackend.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

<<<<<<< HEAD
@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

=======
/**
 * 私信（Message）模块 REST 接口
 * 统一前缀 /api/message
 */
@RestController               // 声明为 SpringMVC 控制器，返回 JSON
@RequestMapping("/api/message")
public class MessageController {

    /* ==================== 依赖注入 ==================== */
    private final MessageService messageService;

    // 构造器注入：方便单元测试，Spring 自动装配
>>>>>>> 7bc3476 (后端代码优化)
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

<<<<<<< HEAD
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Map<String, String> body) {
        String senderId = body.get("senderId");
        String receiverId = body.get("receiverId");
        String content = body.get("content");
        return messageService.sendMessage(senderId, receiverId, content);
    }

    @GetMapping("/between")
    public List<Message> getMessages(@RequestParam String userA, @RequestParam String userB) {
        return messageService.getMessagesBetween(userA, userB);
    }
}
=======
    /**
     * 发送私信
     * POST  http://localhost:8080/api/message/send
     * 请求体 JSON 示例：
     * {
     *   "senderId":   "1",
     *   "receiverId": "2",
     *   "content":    "你好，在吗？"
     * }
     * 返回：刚保存的 Message 实体（含 id、sendTime 等）
     */
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Map<String, String> body) {
        String senderId   = body.get("senderId");
        String receiverId = body.get("receiverId");
        String content    = body.get("content");
        return messageService.sendMessage(senderId, receiverId, content);
    }

    /**
     * 查询两个用户之间的私信列表（按时间正序）
     * GET  http://localhost:8080/api/message/between?userA=1&userB=2
     * 返回：Message 列表，Spring 自动序列化成 JSON 数组
     */
    @GetMapping("/between")
    public List<Message> getMessages(@RequestParam String userA,
                                     @RequestParam String userB) {
        return messageService.getMessagesBetween(userA, userB);
    }
}
>>>>>>> 7bc3476 (后端代码优化)
