package com.matching.post.repository;

import com.matching.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryQuerydsl {
    Page<Post> findAll(Pageable pageable, String keyword);
}
