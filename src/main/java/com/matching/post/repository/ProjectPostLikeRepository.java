package com.matching.post.repository;

import com.matching.post.domain.ProjectPostLike;
import com.matching.post.domain.QnaPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectPostLikeRepository extends JpaRepository<ProjectPostLike, Long> {
    Optional<ProjectPostLike> findByMember_IdAndProjectPost_Id(Long memberId, Long projectPostId);
}
