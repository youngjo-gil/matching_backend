package com.matching.comment.repository;

import com.matching.comment.domain.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
    Optional<QnaComment> findByIdAndAuthor_Id(Long commentId, Long memberId);
}
