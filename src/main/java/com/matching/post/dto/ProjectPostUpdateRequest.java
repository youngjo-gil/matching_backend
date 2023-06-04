package com.matching.post.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostUpdateRequest {
    private String title;
    private String content;
}
