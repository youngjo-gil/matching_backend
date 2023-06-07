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
import com.matching.post.repository.ProjectPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private ProjectPostRepository projectPostRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;


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
    @DisplayName("[ChatRoom] 채팅방 조회 성공")
    void successGetChatRoom() {
        // given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));

        // when & then
        ChatRoom chatRoom = chatRoomService.getChatRoom(1L);

        assertEquals(1L, chatRoom.getId());
        verify(chatRoomRepository).findById(chatRoom.getId());
    }

    @Test
    @DisplayName("[ChatRoom] 채팅방 조회 실패 - 해당 채팅방 아이디 조회 실패 없음")
    void failureGetChatRoomInvalidChatRoomId() {
        // given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> {
            chatRoomService.getChatRoom(1L);
        });
        verify(chatRoomRepository).findById(1L);
    }

    @Test
    @DisplayName("[ChatRoom] 채팅방 생성 성공")
    void successCreateRoom() {
        // given
        given(projectPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong())).willReturn(Optional.of(projectPost));
        given(chatRoomRepository.save(any())).willReturn(chatRoom);

        // when
        Long result = chatRoomService.createRoom(1L, 1L);

        // then
        assertNotNull(result);
        assertEquals(1L, result);
    }

    @Test
    @DisplayName("[ChatRoom] 채팅방 생성 실패 - 해당 작성자가 아닌 경우")
    void failureCreateRoomInvalidAuthor() {
        // given
        given(projectPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong())).willReturn(Optional.empty());
        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            chatRoomService.createRoom(1L, 1L);
        });
        verify(projectPostRepository).findByIdAndAuthor_Id(1L, 1L);
        verifyNoInteractions(chatRoomRepository);
    }

    @Test
    @DisplayName("[ChatRoom] 채팅 메시지 리스트 조회 성공")
    void successGetMessageList() {
        int pageNum = 0;
        int pageSize = 10;

        ChatMessage message1 = ChatMessage.builder()
                .id(1L)
                .message("테스트1")
                .chatRoom(chatRoom)
                .member(member)
                .build();
        ChatMessage message2 = ChatMessage.builder()
                .id(1L)
                .message("테스트2")
                .chatRoom(chatRoom)
                .member(member)
                .build();

        List<ChatMessage> messageList = Arrays.asList(message1, message2);
        Slice<ChatMessage> messageSlice = new PageImpl<>(messageList);

        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));
        given(chatMessageRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoom, PageRequest.of(pageNum, pageSize))).willReturn(messageSlice);

        // when
        Slice<ChatMessageDto> request = messageSlice.map(ChatMessageDto::fromEntity);
        Slice<ChatMessageDto> result = chatRoomService.getMessageList(1L, pageNum, pageSize, member);

        // then
        assertEquals(result.getNumberOfElements(), request.getNumberOfElements());
        assertEquals(result.getContent().get(0).getChatId(), request.getContent().get(0).getChatId());
    }

    @Test
    @DisplayName("[ChatRoom] 채팅 메시지 리스트 조회 실패 - 해당 채팅방이 없는 경우")
    void failureGetMessageListInvalidChatRoom() {
        int pageNum = 0;
        int pageSize = 10;

        // given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            chatRoomService.getMessageList(1L, pageNum, pageSize, member);
        });
    }
}