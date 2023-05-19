package com.matching.post.service;

import com.matching.member.domain.Member;
import com.matching.plan.dto.PlanRequest;
import com.matching.post.domain.PostDocument;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
    Long writePost(PostRequest parameter, String id, List<MultipartFile> multipartFileList);
    PostResponse getPost(Long id);
    Long updatePost(Long postId, Long userId, PostUpdateRequest parameter);

    List<PostDocument> getAuthorByPost(Long userId);
    Long participate(PlanRequest parameter, String email, Long postId);
    void completePlan(String email, Long planId);
}
