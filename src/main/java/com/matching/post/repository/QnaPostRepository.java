package com.matching.post.repository;

import com.matching.post.domain.QnaPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = "select * from qna_post q join qna_post_scrap qps where qps.member_id = :memberId order by q.created_at desc", nativeQuery = true)
    Page<QnaPost> findAllQnaPostByScrapMemberId(Long memberId, Pageable pageable);
}
