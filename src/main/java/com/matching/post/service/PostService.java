package com.matching.post.service;

import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
    Long writePost(PostRequest parameter, String id);
    PostResponse getPost(Long id);
    Long participate(String email, Long postId);
    void completePlan(String email, Long planId);
}
