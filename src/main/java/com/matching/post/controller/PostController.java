package com.matching.post.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.post.dto.PostCategoryRequest;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseDto savePost(
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFile,
            @RequestPart("request") PostRequest parameter,
            @AuthenticationPrincipal User user
    ) {
        String id = user.getUsername();
        Long postId = postService.writePost(parameter, id, multipartFile);

        return ResponseUtil.SUCCESS("글쓰기 성공", postId);
    }

    @GetMapping("/{postId}")
    public ResponseDto getPost(
            @PathVariable Long postId
    ) {
        PostResponse postResponse = postService.getPost(postId);
        return ResponseUtil.SUCCESS("글 조회 성공", postResponse);
    }

    @PatchMapping("/{postId}")
    public ResponseDto updatePost(
        @PathVariable Long postId,
        @RequestBody PostUpdateRequest parameter,
        @AuthenticationPrincipal User user
    ) {
        Long id = postService.updatePost(postId, Long.parseLong(user.getUsername()), parameter);

        return ResponseUtil.SUCCESS("글 수정 성공", id);
    }

    @GetMapping("/categoryList")
    public ResponseDto getPostByCategoryId(
        @RequestBody PostCategoryRequest request
    ) {
        return ResponseUtil.SUCCESS("조회성공", postService.getPostByCategoryDesc(request.getCategoryId()));
    }

    @DeleteMapping("/{postId}")
    public ResponseDto deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal User user
    ) {
        postService.deletePost(postId, Long.parseLong(user.getUsername()));
        return ResponseUtil.SUCCESS("삭제성공", true);
    }

}
