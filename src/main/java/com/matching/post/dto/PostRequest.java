package com.matching.post.dto;

import com.matching.member.domain.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private LocalDate startedAt;
    private LocalDate endedAt;

    private Member member;
}
