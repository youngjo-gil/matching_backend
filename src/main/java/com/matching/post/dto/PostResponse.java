package com.matching.post.dto;

import com.matching.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;

    private LocalDateTime createdAt;

    public static PostResponse from(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor().getNickname())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
