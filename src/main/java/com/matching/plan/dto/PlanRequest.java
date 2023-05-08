package com.matching.plan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PlanRequest {
    private String detail;
    private LocalDate startedAt;
    private LocalDate endedAt;
}
