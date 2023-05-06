package com.matching.chat.service;

import com.matching.chat.domain.ChatRoom;
import com.matching.chat.dto.ChatMessageDto;
import com.matching.member.domain.Member;
import org.springframework.data.domain.Slice;

public interface ChatRoomService {
    ChatRoom getChatRoom(Long chatRoomId);
    Long createRoom(Long postId, Member member);

    Slice<ChatMessageDto> getMessageList(Long chatRoomId, int page, int size, Member member);
}
