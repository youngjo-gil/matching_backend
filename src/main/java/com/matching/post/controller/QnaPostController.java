package com.matching.post.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.service.QnaPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
