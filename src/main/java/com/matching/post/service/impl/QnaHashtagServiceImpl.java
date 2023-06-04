package com.matching.post.service.impl;

import com.matching.post.domain.QnaHashtag;
import com.matching.post.domain.QnaPost;
import com.matching.post.repository.QnaHashtagRepository;
import com.matching.post.service.QnaHashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaHashtagServiceImpl implements QnaHashtagService {
    private final QnaHashtagRepository qnaHashtagRepository;
    private List<QnaHashtag> hashtags = new ArrayList<>();
    @Override
    public void saveQnaHashtag(QnaPost qnaPost, List<String> qnaHashtagList) {
        for (String hashtag: qnaHashtagList) {
            QnaHashtag qnaHashtag = QnaHashtag.from(hashtag, qnaPost);

            hashtags.add(qnaHashtag);
        }
        qnaHashtagRepository.saveAll(hashtags);
    }
}
