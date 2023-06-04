package com.matching.post.repository;

import com.matching.post.domain.QnaHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QnaHashtagRepository extends JpaRepository<QnaHashtag, Long> {
}
