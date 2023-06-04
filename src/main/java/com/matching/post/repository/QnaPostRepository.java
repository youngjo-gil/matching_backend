package com.matching.post.repository;

import com.matching.post.domain.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {
    Optional<QnaPost> findByIdAndAuthor_Id(Long qnaPostId, Long memberId);

}
