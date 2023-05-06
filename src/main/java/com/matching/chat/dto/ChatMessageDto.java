package com.matching.chat.dto;

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
}
