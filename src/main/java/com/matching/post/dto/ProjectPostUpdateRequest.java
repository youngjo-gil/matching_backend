package com.matching.post.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostUpdateRequest {
    @NotBlank(message = "제목 입력은 필수입니다.")
    private String title;
    @NotBlank(message = "내용 입력은 필수입니다.")
    private String body;
    @NotBlank(message = "프로젝트 요약 설명은 필수입니다.")
    private String summary;
}
