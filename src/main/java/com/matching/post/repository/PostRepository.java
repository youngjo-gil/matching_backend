package com.matching.post.repository;

import com.matching.post.domain.Post;
import com.matching.post.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Subgraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndAuthor_Id(Long postId, Long userId);

    @Query(value = "select * from post p\n" +
            "    left join participate p2\n" +
            "        on p.post_id = p2.post_id\n" +
            "    left join photo p3\n" +
            "        on p.post_id = p3.post_id\n" +
            "    where category_id = 1\n" +
            "    and p2.status = 'LEADER' OR p2.status = 'ADMISSION'\n" +
            "    group by p.post_id\n" +
            "    order by count(p2.id) DESC", nativeQuery = true
    )
    Page<Post> findAllOrderByParticipantByPhotoCountByCategoryDesc(@Param("categoryId") Long categoryId, Pageable pageable);

}

