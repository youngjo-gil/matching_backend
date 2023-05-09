package com.matching.post.service;

import com.matching.plan.dto.PlanRequest;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
    Long writePost(PostRequest parameter, String id);
    PostResponse getPost(Long id);
    Long updatePost(Long postId, Long userId, PostUpdateRequest parameter);
    Long participate(PlanRequest parameter, String email, Long postId);
    void completePlan(String email, Long planId);
}
