package com.matching.post.dto;

import com.matching.member.domain.Member;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    /**
     * 글쓰기
     */
    private String title;
    private String body;
    private String summary;
    private Member member;
    private Long categoryId;


    private String planBody;
    private LocalDate startedAt;
    private LocalDate endedAt;

}
