package com.matching.chat.service;

public interface ChatService {
    void sendMessage(Long chatRoomId, Long senderId, String message);
}
