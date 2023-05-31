package com.matching.post.repository;

import com.matching.post.domain.Post;
import com.matching.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndAuthor_Id(Long postId, Long userId);

    @Query("SELECT p FROM Post p LEFT JOIN p.participateList pt WHERE p.category.id = :categoryId GROUP BY p ORDER BY COUNT (pt.id) ASC")
    List<Post> findAllOrderByParticipantCountByCategoryAsc(@Param("categoryId") Long categoryId);
}

