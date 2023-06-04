package com.matching.post.service;

import com.matching.post.dto.QnaPostRequest;
import org.springframework.stereotype.Service;

public interface QnaPostService {
    Long writeQna(QnaPostRequest parameter, Long memberId);
}
