package com.matching.plan.service;

import com.matching.plan.dto.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getPlanList(Long postId);
}
