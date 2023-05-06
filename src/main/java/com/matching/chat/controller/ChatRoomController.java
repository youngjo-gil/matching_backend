package com.matching.chat.controller;

import com.matching.chat.dto.ChatRoomDto;
import com.matching.chat.service.ChatRoomService;
import com.matching.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<Long> createChatRoom(
            @RequestBody ChatRoomDto parameter,
            @AuthenticationPrincipal Member member
    ) {
        Long chatRoomId = chatRoomService.createRoom(parameter.getPostId(), member);

        return ResponseEntity.ok(chatRoomId);
    }
}
