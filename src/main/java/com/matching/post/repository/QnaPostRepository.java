package com.matching.post.repository;

import com.matching.post.domain.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {


}
