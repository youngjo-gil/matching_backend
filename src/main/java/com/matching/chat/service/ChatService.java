package com.matching.chat.service;

import com.matching.chat.domain.ChatRoom;
import com.matching.member.domain.Member;
import org.springframework.stereotype.Service;

public interface ChatService {
    ChatRoom getChatRoom(Long chatRoomId);

    Long createRoom(Long postId, Member member);
}
