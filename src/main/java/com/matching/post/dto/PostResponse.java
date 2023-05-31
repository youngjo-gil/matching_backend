package com.matching.post.dto;

import com.matching.post.domain.Post;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String categoryName;
    private String plan;
    private String author;
    private LocalDateTime createdAt;
    private LocalDate startedAt;
    private LocalDate endedAt;

    private List<String> photoList;

    public static PostResponse from(Post post, List<String> test) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategory().getCategoryName())
                .plan(post.getPlan().getDetail())
                .author(post.getAuthor().getNickname())
                .createdAt(post.getCreatedAt())
                .startedAt(post.getPlan().getStartedAt())
                .endedAt(post.getPlan().getEndedAt())
                .photoList(test)
                .build();
    }
}
