package com.matching.chat.controller;

import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.service.ChatService;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    @MessageMapping("/chat/message")
    public void message(
            ChatMessageDto parameter
    ) {
        Member member = memberRepository.findById(parameter.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        chatService.sendMessage(parameter.getChatRoomId(), member.getId(), parameter.getMessage());
    }

}