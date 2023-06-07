package com.matching.plan.service.impl;

import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.plan.domain.Plan;
import com.matching.plan.dto.PlanResponse;
import com.matching.plan.repository.PlanRepository;
import com.matching.plan.service.PlanService;
import com.matching.post.domain.ProjectPost;
import com.matching.post.repository.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final ProjectPostRepository projectPostRepository;

    @Override
    public PlanResponse getPlanList(Long postId) {
        ProjectPost projectPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Plan plan = planRepository.findByProjectPost_Id(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

        return PlanResponse.of(plan, projectPost);
    }
}
