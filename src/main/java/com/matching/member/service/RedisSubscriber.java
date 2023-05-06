package com.matching.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matching.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String pubMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());

            ChatMessageDto chatMessageDto = objectMapper.readValue(pubMessage, ChatMessageDto.class);


            messagingTemplate.convertAndSend("/chatrooms/" + chatMessageDto.getChatRoomId(), chatMessageDto);

        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }
}
