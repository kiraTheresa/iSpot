package com.ispot.ispotbackend.repository;

import com.ispot.ispotbackend.model.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MessageRepository {
    private final Map<String, Message> messages = new HashMap<>();

    public void save(Message message) {
        messages.put(message.getId(), message);
    }

    public List<Message> findAll() {
        return new ArrayList<>(messages.values());
    }

    public List<Message> findBySenderId(String senderId) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            if (m.getSenderId().equals(senderId)) {
                list.add(m);
            }
        }
        return list;
    }

    public List<Message> findByReceiverId(String receiverId) {
        List<Message> list = new ArrayList<>();
        for (Message m : messages.values()) {
            if (m.getReceiverId().equals(receiverId)) {
                list.add(m);
            }
        }
        return list;
    }

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
