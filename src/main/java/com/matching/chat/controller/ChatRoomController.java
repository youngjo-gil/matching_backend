package com.matching.chat.controller;

import com.matching.chat.dto.ChatMessageDto;
import com.matching.chat.service.ChatRoomService;
import com.matching.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/{postId}")
    public ResponseEntity<?> createChatRoom(
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        Long userId = Long.parseLong(user.getUsername());
        Long chatRoomId = chatRoomService.createRoom(postId, userId);

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
