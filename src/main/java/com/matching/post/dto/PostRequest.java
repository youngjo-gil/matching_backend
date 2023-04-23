package com.matching.post.dto;

import com.matching.member.domain.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private String title;
    private String content;

    private Member member;
}
