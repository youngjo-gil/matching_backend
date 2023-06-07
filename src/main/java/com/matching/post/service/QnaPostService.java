package com.matching.post.service;

import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;
import org.springframework.data.domain.Page;

public interface QnaPostService {
    Long writeQna(QnaPostRequest parameter, Long memberId);

    QnaPostResponse getQna(Long qnaPostId);
    Long updateQna(QnaPostRequest parameter, Long memberId, Long qnaPostId);
    Long deleteQna(Long memberId, Long qnaPostId);
    void toggleLike(Long memberId, Long qnaPostId);

    void toggleScrap(Long memberId, Long qnaPostId);

    // 스크랩 게시글 조회
    Page<QnaPostResponse> getPostByScrap(Long memberId, int pageNum);
}
