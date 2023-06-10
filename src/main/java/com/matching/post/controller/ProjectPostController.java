package com.matching.post.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.post.dto.*;
import com.matching.post.service.ProjectPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class ProjectPostController {
    private final ProjectPostService projectPostService;
    @PostMapping("/write")
    public ResponseDto savePost(
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFile,
            @RequestPart("request") @Valid ProjectPostRequest parameter,
            @AuthenticationPrincipal User user
    ) {
        String id = user.getUsername();
        Long postId = projectPostService.writePost(parameter, id, multipartFile);

        return ResponseUtil.SUCCESS("글쓰기 성공", postId);
    }

    @GetMapping("/{postId}")
    public ResponseDto getPost(
            @PathVariable Long postId
    ) {
        ProjectPostResponse projectPostResponse = projectPostService.getPost(postId);
        return ResponseUtil.SUCCESS("글 조회 성공", projectPostResponse);
    }

    @PatchMapping("/{postId}")
    public ResponseDto updatePost(
        @PathVariable Long postId,
        @RequestBody @Valid ProjectPostUpdateRequest parameter,
        @AuthenticationPrincipal User user
    ) {
        Long id = projectPostService.updatePost(postId, Long.parseLong(user.getUsername()), parameter);

        return ResponseUtil.SUCCESS("글 수정 성공", id);
    }

    @GetMapping("/categoryList")
    public ResponseDto getPostByCategoryId(
        @RequestBody ProjectPostCategoryRequest request
    ) {
        return ResponseUtil.SUCCESS("조회성공", projectPostService.getPostByCategoryDesc(request.getCategoryId()));
    }

    @DeleteMapping("/{postId}")
    public ResponseDto deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal User user
    ) {
        projectPostService.deletePost(postId, Long.parseLong(user.getUsername()));
        return ResponseUtil.SUCCESS("삭제성공", true);
    }


    @GetMapping("/search")
    public ResponseDto getPostSearchList(
            @RequestBody @Valid PostSearchRequest parameter
    ) {
        return ResponseUtil.SUCCESS("조회성공", projectPostService.getPostSearchList(parameter));
    }

    // 참가 중인 projectPost 조회
    @GetMapping("/myPage/participant")
    public ResponseDto getPostByParticipant(
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseUtil.SUCCESS("목록 조회 성공", projectPostService.getPostByParticipant(memberId));
    }

    // 작성한 projectPost 조회
    @GetMapping("/myPage/write")
    public ResponseDto getPostByWrite(
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        return ResponseUtil.SUCCESS("목록 조회 성공", projectPostService.getPostByWrite(memberId));
    }

    // 스크랩한 qna 게시글 조회

}
