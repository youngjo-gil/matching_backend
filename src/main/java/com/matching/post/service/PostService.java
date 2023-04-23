package com.matching.post.service;

import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
    boolean writePost(PostRequest parameter, HttpServletRequest request);
    PostResponse getPost(Long id);
    Long participate(String email, Long postId);
}
