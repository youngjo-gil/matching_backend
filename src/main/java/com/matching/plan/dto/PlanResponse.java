package com.matching.plan.dto;

import com.matching.member.domain.Member;
import com.matching.plan.domain.Plan;
import com.matching.post.domain.ProjectPost;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private Long id;
    private String detail;
    private boolean completed;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private Long postId;
    private List<Member> participantList;

    public static PlanResponse of(Plan plan, ProjectPost projectPost) {
        return PlanResponse.builder()
                .id(plan.getId())
                .detail(plan.getDetail())
                .completed(plan.isCompleted())
                .startedAt(plan.getStartedAt())
                .endedAt(plan.getEndedAt())
                .postId(projectPost.getId())
                .build();
    }
}
