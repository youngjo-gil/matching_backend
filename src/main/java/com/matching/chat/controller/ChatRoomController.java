package com.matching.chat.controller;

import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.dto.ChatRoomDto;
import com.matching.chat.service.ChatRoomService;
import com.matching.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{chatRoomId}/pub")
    public ResponseEntity<Slice<ChatMessageDto>> getMessageList(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Member member
    ) {
        Slice<ChatMessageDto> chatMessageDtos = chatRoomService.getMessageList(chatRoomId, page, size, member);

        return ResponseEntity.ok(chatMessageDtos);
    }
}
