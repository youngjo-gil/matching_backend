package com.matching.post.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;
import com.matching.post.service.QnaPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnaPostController {
    private final QnaPostService qnaPostService;

    @PostMapping("/write")
    public ResponseDto saveQnaPost(
            @RequestBody @Valid QnaPostRequest parameter,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        Long qnaPostId = qnaPostService.writeQna(parameter, memberId);

        return ResponseUtil.SUCCESS("글쓰기 성공", qnaPostId);
    }

    @GetMapping("/{qnaPostId}")
    public ResponseDto getQnaPost(
            @PathVariable Long qnaPostId
    ) {
        QnaPostResponse response = qnaPostService.getQna(qnaPostId);

        return ResponseUtil.SUCCESS("게시글 조회 성공", response);
    }

    @PatchMapping("/{qnaPostId}")
    public ResponseDto updateQnaPost(
            @RequestBody @Valid QnaPostRequest parameter,
            @PathVariable Long qnaPostId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());

        Long qnaPost = qnaPostService.updateQna(parameter, memberId, qnaPostId);

        return ResponseUtil.SUCCESS("업데이스 성공", qnaPost);
    }

    @DeleteMapping("/{qnaPostId}")
    public ResponseDto deletePost(
            @PathVariable Long qnaPostId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());

        qnaPostService.deleteQna(memberId, qnaPostId);

        return ResponseUtil.SUCCESS("게시글 삭제 성공", true);
    }

    @PostMapping("/like/{qnaPostId}")
    public ResponseDto toggleQnaPostLike(
            @PathVariable Long qnaPostId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        qnaPostService.toggleLike(memberId, qnaPostId);

        return ResponseUtil.SUCCESS("좋아요 성공", true);
    }

    @PostMapping("/scrap/{qnaPostId}")
    public ResponseDto toggleQnaPostScrap(
            @PathVariable Long qnaPostId,
            @AuthenticationPrincipal User user
    ) {
        Long memberId = Long.parseLong(user.getUsername());
        qnaPostService.toggleScrap(memberId, qnaPostId);

        return ResponseUtil.SUCCESS("스크랩 성공", true);
    }
}
