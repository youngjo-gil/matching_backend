package com.matching.participate.repository;

import com.matching.participate.domain.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    Optional<Participate> findByParticipate_IdAndPost_Id(Long userId, Long postId);
}
