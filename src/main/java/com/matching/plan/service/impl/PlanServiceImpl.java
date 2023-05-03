package com.matching.plan.service.impl;

import com.matching.member.repository.MemberRepository;
import com.matching.plan.domain.Plan;
import com.matching.plan.dto.PlanResponse;
import com.matching.plan.repository.PlanRepository;
import com.matching.plan.service.PlanService;
import com.matching.post.domain.Post;
import com.matching.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final PostRepository postRepository;

    @Override
    public List<PlanResponse> getPlanList(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        List<Plan> plans = planRepository.findAllByPost_Id(postId)
                .orElseThrow(() -> new IllegalArgumentException("목표 설정이 없습니다."));

        return plans.stream()
                .map(PlanResponse::new)
                .collect(Collectors.toList());
    }
}
