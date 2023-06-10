package com.matching.post.dto;

import com.matching.member.domain.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectPostRequest {
    @NotBlank(message = "제목 입력은 필수입니다.")
    private String title;
    @NotBlank(message = "내용 입력은 필수입니다.")
    private String body;
    @NotBlank(message = "프로젝트 요약 설명은 필수입니다.")
    private String summary;
    private Member member;
    private Long categoryId;

    @NotBlank(message = "묙포 설정은 필수입니다.")
    private String planBody;
    private LocalDate startedAt;
    private LocalDate endedAt;
}
