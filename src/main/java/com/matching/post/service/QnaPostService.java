package com.matching.post.service;

import com.matching.post.dto.QnaPostRequest;

public interface QnaPostService {
    Long writeQna(QnaPostRequest parameter, Long memberId);
    Long updateQna(QnaPostRequest parameter, Long memberId, Long qnaPostId);
}
