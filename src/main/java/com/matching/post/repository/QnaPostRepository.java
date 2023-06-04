package com.matching.post.repository;

import com.matching.post.domain.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {
    Optional<QnaPost> findByIdAndAuthor_Id(Long qnaPostId, Long memberId);

    @Query("select count(pl) from QnaPostLike pl where pl.qnaPost.id = :qnaPostId")
    Long getLikeCountByQnaPostId(@Param("qnaPostId") Long qnaPostId);

}
