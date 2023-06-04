package com.matching.post.repository;

import com.matching.post.domain.ProjectPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectPostRepository extends JpaRepository<ProjectPost, Long> {
    Optional<ProjectPost> findByIdAndAuthor_Id(Long postId, Long userId);

    // 카테고리별 참가자 순 조회
    @Query(value = "select * from post p\n" +
            "    left join participate p2\n" +
            "        on p.post_id = p2.post_id\n" +
            "    left join photo p3\n" +
            "        on p.post_id = p3.post_id\n" +
            "    where p.category_id = :categoryId\n" +
            "       and (p2.status = 'LEADER' OR p2.status = 'ADMISSION')\n" +
            "    group by p.post_id\n" +
            "    order by count(p2.id) DESC", nativeQuery = true
    )
    Page<ProjectPost> findAllOrderByParticipantByPhotoCountByCategoryDesc(@Param("categoryId") Long categoryId, Pageable pageable);

    // 해당 회원 참가중인 projectPost 조회
    @Query(value = "SELECT p FROM ProjectPost p LEFT JOIN p.participateList p2 WHERE p2.participate.id = :memberId AND (p2.status = 'LEADER' OR p2.status = 'ADMISSION') ORDER BY p.createdAt DESC")
    Page<ProjectPost> findAllOrderByParticipateByPhotoCreatedAtDesc(@Param("memberId") Long memberId, Pageable pageable);

    // 작성한 글 조회
    Page<ProjectPost> findAllByAuthor_Id(Long memberId, Pageable pageable);

}

