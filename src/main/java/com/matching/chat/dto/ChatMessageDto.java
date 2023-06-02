package com.matching.chat.dto;

import com.matching.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto implements Serializable {
    private Long chatId;
    private Long chatRoomId;
    private String message;
    private Long userId;
    private String nickname;

    public static ChatMessageDto fromEntity(ChatMessage parameter) {
        return ChatMessageDto.builder()
                .chatId(parameter.getId())
                .chatRoomId(parameter.getChatRoom().getId())
                .message(parameter.getMessage())
                .userId(parameter.getMember().getId())
                .build();
    }
}
