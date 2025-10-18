package com.ispot.ispotbackend.service.impl;

import com.ispot.ispotbackend.model.entity.Message;
import com.ispot.ispotbackend.repository.MessageRepository;
import com.ispot.ispotbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Message> getMessagesBetween(String userA, String userB) {
        return messageRepository.findByUserPair(userA, userB);
    }
}
