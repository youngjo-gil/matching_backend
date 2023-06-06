package com.matching.post.dto;

import com.matching.photo.domain.Photo;
import com.matching.post.domain.ProjectPost;
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
public class ProjectPostResponse {
    private Long id;
    private String title;
    private String body;

    private String categoryName;
    private String plan;
    private String author;
    private LocalDateTime createdAt;
    private LocalDate startedAt;
    private LocalDate endedAt;

    private List<String> photoList;

    public static ProjectPostResponse from(ProjectPost projectPost, List<String> photoList) {
        return ProjectPostResponse.builder()
                .id(projectPost.getId())
                .title(projectPost.getTitle())
                .body(projectPost.getBody())
                .categoryName(projectPost.getCategory().getCategoryName())
                .plan(projectPost.getPlan().getPlanBody())
                .createdAt(projectPost.getCreatedAt())
                .startedAt(projectPost.getPlan().getStartedAt())
                .endedAt(projectPost.getPlan().getEndedAt())
                .photoList(photoList)
                .build();
    }

    public static Page<ProjectPostResponse> fromEntitiesPage(Page<ProjectPost> postPage) {
        return new PageImpl<>(
                postPage.stream()
                        .map(post -> {
                            List<String> photoList = post.getPhotoList().stream()
                                    .map(Photo::getPathname)
                                    .collect(Collectors.toList());
                            return ProjectPostResponse.from(post, photoList);
                        })
                        .collect(Collectors.toList()),
                postPage.getPageable(),
                postPage.getTotalElements()
        );
    }
}
