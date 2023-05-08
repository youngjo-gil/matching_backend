package com.matching.plan.dto;

import com.matching.member.domain.Member;
import com.matching.plan.domain.Plan;
import com.matching.post.domain.Post;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static PlanResponse of(Plan plan, Post post) {
        return PlanResponse.builder()
                .id(plan.getId())
                .detail(plan.getDetail())
                .completed(plan.isCompleted())
                .startedAt(plan.getStartedAt())
                .endedAt(plan.getEndedAt())
                .postId(post.getId())
                .build();
    }
}
