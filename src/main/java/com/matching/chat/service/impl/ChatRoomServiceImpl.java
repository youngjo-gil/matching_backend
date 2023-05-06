package com.matching.chat.service.impl;

import com.matching.chat.domain.ChatRoom;
import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.repository.ChatMessageRepository;
import com.matching.chat.repository.ChatRoomRepository;
import com.matching.chat.service.ChatRoomService;
import com.matching.member.domain.Member;
import com.matching.post.domain.Post;
import com.matching.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;
    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 없습니다."));
        return chatRoom;
    }

    @Override
    public Long createRoom(Long postId, Member member) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 게시글이 없습니다."));

        return chatRoomRepository.save(ChatRoom.builder()
                .member(member)
                .post(post)
                .build()).getId();
    }

    @Override
    public Slice<ChatMessageDto> getMessageList(Long chatRoomId, int page, int size, Member member) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다."));

        return chatMessageRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoom, PageRequest.of(page, size))
                .map(chatMessage -> ChatMessageDto.fromEntity(chatMessage));
    }
}
