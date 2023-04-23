package com.matching.plan.repository;

import com.matching.plan.domain.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
