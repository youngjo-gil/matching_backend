package com.matching.post.repository;

import com.matching.post.domain.QnaPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaPostLikeRepository extends JpaRepository<QnaPostLike, Long> {
    Optional<QnaPostLike>  findByMember_IdAndQnaPost_Id(Long memberId, Long qnaPostId);
}
