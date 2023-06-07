package com.matching.chat.service.impl;

import com.matching.chat.domain.ChatMessage;
import com.matching.chat.domain.ChatRoom;
import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.repository.ChatMessageRepository;
import com.matching.chat.repository.ChatRoomRepository;
import com.matching.member.domain.Member;
import com.matching.member.domain.MemberRole;
import com.matching.member.domain.MemberStatus;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.ProjectPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ChannelTopic channelTopic;


    @InjectMocks
    private ChatServiceImpl chatService;

    Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("1111")
            .name("test")
            .status(MemberStatus.REGISTERED)
            .role(MemberRole.USER)
            .build();
    ProjectPost projectPost = ProjectPost.builder()
            .id(1L)
            .title("스터디 모집")
            .body("백엔드 스터디")
            .author(member)
            .build();
    ChatRoom chatRoom = ChatRoom.builder()
            .id(1L)
            .projectPost(projectPost)
            .member(member)
            .build();

    @Test
    @DisplayName("[Chat] 메시지 전송 성공")
    void successSendMessage() {
        ChatMessage chatMessage = ChatMessage.builder()
                .id(1L)
                .message("Hello! Matching!")
                .chatRoom(chatRoom)
                .member(member)
                .build();

        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(chatMessageRepository.save(any())).willReturn(chatMessage);
        given(channelTopic.getTopic()).willReturn("chatroom");
        given(channelTopic.getTopic()).willReturn("onMessage");

        // when
        ArgumentCaptor<ChatMessage> chatMessageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        chatService.sendMessage(1L, 1L, "Hello! Matching!");

        // then
        verify(chatMessageRepository, times(1)).save(chatMessageCaptor.capture());
        assertEquals("Hello! Matching!", chatMessageCaptor.getValue().getMessage());
        assertEquals(chatRoom, chatMessageCaptor.getValue().getChatRoom());
        assertEquals(member, chatMessageCaptor.getValue().getMember());
    }

    @Test
    @DisplayName("[Chat] 메시지 전송 실패 - 채팅방 존재하지 않음")
    void failureSendMessageChatRoomNotFound() {
        // given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> chatService.sendMessage(1L, 1L, "Hello! Matching!"));
    }

    @Test
    @DisplayName("[Chat] 메시지 전송 실패 - 회원 존재하지 않음")
    void failureSendMessageMemberNotFound() {
        // given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());
        // when & then
        assertThrows(RuntimeException.class, () -> chatService.sendMessage(1L, 1L, "Hello! Matching!"));
    }
}