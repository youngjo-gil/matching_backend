package com.matching.chat.service.impl;

import com.matching.chat.domain.ChatRoom;
import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.repository.ChatMessageRepository;
import com.matching.chat.repository.ChatRoomRepository;
import com.matching.chat.service.ChatRoomService;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.ProjectPost;
import com.matching.post.repository.ProjectPostRepository;
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
    private final ProjectPostRepository projectPostRepository;
    private final MemberRepository memberRepository;
    @Override
    public ChatRoom getChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 없습니다."));
        return chatRoom;
    }

    @Override
    public Long createRoom(Long postId, Long userId) {
        ProjectPost projectPost = projectPostRepository.findByIdAndAuthor_Id(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("글작성자만 채팅방 개설이 가능합니다."));

        if(!projectPost.getAuthor().getId().equals(userId)) {
            throw new IllegalArgumentException("");
        }

        return chatRoomRepository.save(ChatRoom.builder()
                .member(projectPost.getAuthor())
                .projectPost(projectPost)
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
