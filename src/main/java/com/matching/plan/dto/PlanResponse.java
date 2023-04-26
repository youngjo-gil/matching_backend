package com.matching.plan.dto;

import com.matching.plan.domain.Plan;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private Long id;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int participantCount;

    public PlanResponse(Plan plan) {
    }
}
