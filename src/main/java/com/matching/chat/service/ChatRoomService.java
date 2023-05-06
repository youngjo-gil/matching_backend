package com.matching.chat.service;

import com.matching.chat.domain.ChatRoom;
import com.matching.member.domain.Member;

public interface ChatRoomService {
    ChatRoom getChatRoom(Long chatRoomId);

    Long createRoom(Long postId, Member member);
}
