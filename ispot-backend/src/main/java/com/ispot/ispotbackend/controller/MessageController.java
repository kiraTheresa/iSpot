package com.ispot.ispotbackend.controller;

import com.ispot.ispotbackend.model.entity.Message;
import com.ispot.ispotbackend.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

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
