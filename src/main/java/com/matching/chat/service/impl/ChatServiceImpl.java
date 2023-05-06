package com.matching.chat.service.impl;

import com.matching.chat.domain.ChatMessage;
import com.matching.chat.domain.ChatRoom;
import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.repository.ChatMessageRepository;
import com.matching.chat.repository.ChatRoomRepository;
import com.matching.chat.service.ChatService;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    @Override
    public void sendMessage(Long chatRoomId, Long senderId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));

        Member member = memberRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        ChatMessageDto dto = ChatMessageDto.fromEntity(chatMessageRepository.save(
                ChatMessage.builder()
                        .message(message)
                        .chatRoom(chatRoom)
                        .member(member)
                        .build()
        ));

        redisTemplate.convertAndSend(channelTopic.getTopic(), dto);
    }

}
