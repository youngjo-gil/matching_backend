package com.matching.post.controller;

import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseEntity<?> savePost(
            @RequestBody PostRequest parameter,
            @AuthenticationPrincipal User user
    ) {
        String id = user.getUsername();
        Long postId = postService.writePost(parameter, id);

        return ResponseEntity.ok().body(postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(
            @PathVariable Long postId
    ) {
        PostResponse postResponse = postService.getPost(postId);

        return ResponseEntity.ok().body(postResponse);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> updatePost(
        @PathVariable Long postId,
        @RequestBody PostUpdateRequest parameter,
        @AuthenticationPrincipal User user
    ) {
        Long id = postService.updatePost(postId, Long.parseLong(user.getUsername()), parameter);

        return ResponseEntity.ok().body(id);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal User user
    ) {
        return null;
    }
}
