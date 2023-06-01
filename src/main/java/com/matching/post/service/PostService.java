package com.matching.post.service;

import com.matching.plan.dto.PlanRequest;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Long writePost(PostRequest parameter, String id, List<MultipartFile> multipartFile);
    PostResponse getPost(Long id);
    Long updatePost(Long postId, Long userId, PostUpdateRequest parameter);
    void deletePost(Long postId, Long userId);

    Page<PostResponse> getPostByCategoryDesc(Long categoryId);
    Long participate(PlanRequest parameter, String email, Long postId);
    void completePlan(String email, Long planId);
}
