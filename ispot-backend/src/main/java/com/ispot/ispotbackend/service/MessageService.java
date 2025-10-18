package com.ispot.ispotbackend.service;

import com.ispot.ispotbackend.model.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(String senderId, String receiverId, String content);
    List<Message> getMessagesBetween(String userA, String userB);
}
