package com.matching.comment.controller;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.dto.CommentRequest;
import com.matching.comment.service.CommentService;
import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseDto createComment(
            @RequestBody CommentRequest parameter,
            @PathVariable Long postId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());

        QnaComment qnaComment = commentService.createQnaComment(parameter, postId, memberId);

        return ResponseUtil.SUCCESS("댓글 생성 성공", qnaComment);
    }

    @PatchMapping("/{commentId}")
    public ResponseDto updateComment(
            @RequestBody CommentRequest parameter,
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        QnaComment qnaComment = commentService.updateQnaComment(parameter, commentId, memberId);

        return ResponseUtil.SUCCESS("댓글 수정 성공", qnaComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseDto deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        commentService.removeQnaComment(commentId, memberId);

        return ResponseUtil.SUCCESS("댓글 삭제 성공", true);
    }
}
