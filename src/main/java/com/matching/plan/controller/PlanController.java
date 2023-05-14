package com.matching.plan.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.plan.dto.PlanResponse;
import com.matching.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plan")
public class PlanController {
    private final PlanService planService;
    @GetMapping("/{postId}")
    public ResponseDto getPlan(
            @PathVariable Long postId
    ) {
        PlanResponse response = planService.getPlanList(postId);

        return ResponseUtil.SUCCESS("목표 리스트 조회 성공", response);
    }
}
