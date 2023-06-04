package com.matching.post.service.impl;

import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaPost;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.repository.QnaPostRepository;
import com.matching.post.service.QnaHashtagService;
import com.matching.post.service.QnaPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QnaPostServiceImpl implements QnaPostService {
    private final MemberRepository memberRepository;
    private final QnaPostRepository qnaPostRepository;
    private final QnaHashtagService qnaHashtagService;

    @Transactional
    @Override
    public Long writeQna(QnaPostRequest parameter, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        parameter.setMember(member);

        QnaPost qnaPost = qnaPostRepository.save(QnaPost.from(parameter));

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        return qnaPost.getId();
    }

    @Transactional
    @Override
    public Long updateQna(QnaPostRequest parameter, Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        qnaPost.getHashtags().clear();

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        return qnaPost.getId();
    }
}
