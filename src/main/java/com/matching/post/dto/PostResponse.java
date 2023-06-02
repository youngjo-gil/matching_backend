package com.matching.post.dto;

import com.matching.photo.domain.Photo;
import com.matching.post.domain.Post;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static PostResponse from(Post post, List<String> photoList) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .categoryName(post.getCategory().getCategoryName())
                .plan(post.getPlan().getDetail())
                .createdAt(post.getCreatedAt())
                .startedAt(post.getPlan().getStartedAt())
                .endedAt(post.getPlan().getEndedAt())
                .photoList(photoList)
                .build();
    }

    public static Page<PostResponse> fromEntitiesPage(Page<Post> postPage) {
        return new PageImpl<>(
                postPage.stream()
                        .map(post -> {
                            List<String> photoList = post.getPhotoList().stream()
                                    .map(Photo::getPathname)
                                    .collect(Collectors.toList());
                            return PostResponse.from(post, photoList);
                        })
                        .collect(Collectors.toList()),
                postPage.getPageable(),
                postPage.getTotalElements()
        );
    }
}
