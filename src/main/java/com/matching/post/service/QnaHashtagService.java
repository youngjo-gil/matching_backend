package com.matching.post.service;

import com.matching.post.domain.QnaHashtag;
import com.matching.post.domain.QnaPost;

import java.util.List;

public interface QnaHashtagService {
    void saveQnaHashtag(QnaPost qnaPost, List<String> qnaHashtagList);
}
