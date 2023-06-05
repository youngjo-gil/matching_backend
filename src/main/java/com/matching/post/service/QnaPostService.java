package com.matching.post.service;

import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;

public interface QnaPostService {
    Long writeQna(QnaPostRequest parameter, Long memberId);

    QnaPostResponse getQna(Long qnaPostId);
    Long updateQna(QnaPostRequest parameter, Long memberId, Long qnaPostId);
    void deleteQna(Long memberId, Long qnaPostId);
    void toggleLike(Long memberId, Long qnaPostId);

    void toggleScrap(Long memberId, Long qnaPostId);
}
