package com.matching.plan.service.impl;

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
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Plan plan = planRepository.findByPost_Id(postId)
                .orElseThrow(() -> new IllegalArgumentException("목표 설정이 없습니다."));

        return PlanResponse.of(plan, projectPost);
    }
}
