package com.matching.post.controller;

import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
            @AuthenticationPrincipal User user
    ) {
        String id = user.getUsername();
        Long postId = postService.writePost(parameter, id);

        return ResponseEntity.ok().body(postId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable Long id
    ) {
        PostResponse postResponse = postService.getPost(id);

        return ResponseEntity.ok().body(postResponse);
    }
}
