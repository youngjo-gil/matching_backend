package com.matching.post.repository;

import com.matching.post.domain.ProjectPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuerydsl {
    Page<ProjectPost> findAll(Pageable pageable, String keyword);
}
