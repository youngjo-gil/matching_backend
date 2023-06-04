package com.matching.plan.repository;

import com.matching.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByProjectPost_Id(Long postId);
}
