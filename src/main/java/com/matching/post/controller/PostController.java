package com.matching.post.controller;

import com.matching.post.dto.PostRequest;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        boolean savePostComplete = postService.writePost(parameter, request);

        if(savePostComplete) {
            return ResponseEntity.ok().body("글 작성 성공");
        } else {
            return ResponseEntity.badRequest().body("글 작성 실패");
        }
    }


}
