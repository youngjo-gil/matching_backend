package com.matching.comment.service;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.dto.CommentRequest;
import com.matching.comment.dto.CommentResponse;

public interface CommentService {
    QnaComment createQnaComment(CommentRequest parameter, Long postId, Long memberId);
    QnaComment updateQnaComment(CommentRequest parameter, Long commentId, Long memberId);
    void removeQnaComment(Long commentId, Long memberId);
}
