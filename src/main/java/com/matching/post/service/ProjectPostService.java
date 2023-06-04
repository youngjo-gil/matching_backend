package com.matching.post.service;

import com.matching.plan.dto.PlanRequest;
import com.matching.post.dto.ProjectPostRequest;
import com.matching.post.dto.ProjectPostResponse;
import com.matching.post.dto.PostSearchRequest;
import com.matching.post.dto.ProjectPostUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProjectPostService {
    Long writePost(ProjectPostRequest parameter, String id, List<MultipartFile> multipartFile);
    ProjectPostResponse getPost(Long id);
    Long updatePost(Long postId, Long userId, ProjectPostUpdateRequest parameter);
    void deletePost(Long postId, Long userId);
    Page<ProjectPostResponse> getPostByCategoryDesc(Long categoryId);
//    Page<ProjectPostResponse> getPostSearchList(PostSearchRequest parameter);

    // 참가 중인 projectPost 조회
    Page<ProjectPostResponse> getPostByParticipant(Long memberId);
    // 작성한 글 조회
    Page<ProjectPostResponse> getPostByWrite(Long memberId);
    Long participate(PlanRequest parameter, String email, Long postId);
    void completePlan(String email, Long planId);
}
