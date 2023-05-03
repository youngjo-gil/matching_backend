package com.matching.post.controller;

import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseEntity<?> savePost(
            @RequestBody PostRequest parameter,
            HttpServletRequest request
    ) {
        Long postId = postService.writePost(parameter, request);

        return ResponseEntity.ok().body(postId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        PostResponse postResponse = postService.getPost(id);

        return ResponseEntity.ok().body(postResponse);
    }
}
