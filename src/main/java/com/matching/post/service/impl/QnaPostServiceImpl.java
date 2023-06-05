package com.matching.post.service.impl;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.repository.QnaCommentRepository;
import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaPost;
import com.matching.post.domain.QnaPostLike;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;
import com.matching.post.repository.QnaPostLikeRepository;
import com.matching.post.repository.QnaPostRepository;
import com.matching.post.service.QnaHashtagService;
import com.matching.post.service.QnaPostService;
import com.matching.scrap.domain.QnaPostScrap;
import com.matching.scrap.repostory.QnaPostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaPostServiceImpl implements QnaPostService {
    private final MemberRepository memberRepository;
    private final QnaPostRepository qnaPostRepository;
    private final QnaHashtagService qnaHashtagService;
    private final QnaPostLikeRepository qnaPostLikeRepository;
    private final QnaCommentRepository qnaCommentRepository;
    private final QnaPostScrapRepository qnaPostScrapRepository;

    @Transactional
    @Override
    public Long writeQna(QnaPostRequest parameter, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        parameter.setMember(member);

        QnaPost qnaPost = qnaPostRepository.save(QnaPost.from(parameter));

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        return qnaPost.getId();
    }

    @Override
    public QnaPostResponse getQna(Long qnaPostId) {
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long likeCount = getLikeCount(qnaPost.getId());
        List<QnaComment> qnaCommentList = new ArrayList<>();

        Optional<List<QnaComment>> optionalQnaComments = qnaCommentRepository.findAllByQnaPost_Id(qnaPost.getId());

        if(optionalQnaComments.isPresent()) {
            qnaCommentList = optionalQnaComments.get();
        }

        return QnaPostResponse.from(qnaPost, likeCount, qnaCommentList);
    }

    @Transactional
    @Override
    public Long updateQna(QnaPostRequest parameter, Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        qnaPost.getHashtags().clear();

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        return qnaPost.getId();
    }

    @Override
    public void deleteQna(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        qnaPostRepository.delete(qnaPost);
    }

    @Transactional
    @Override
    public void toggleLike(Long memberId, Long qnaPostId) {
        Optional<QnaPostLike> qnaPostLikeOptional = qnaPostLikeRepository.findByMember_IdAndQnaPost_Id(memberId, qnaPostId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(qnaPostLikeOptional.isPresent()){
            // 좋아요가 있는경우 취소처리
            qnaPostLikeRepository.delete(qnaPostLikeOptional.get());
        } else {
            QnaPostLike qnaPostLike = QnaPostLike.from(member, qnaPost);
            qnaPostLikeRepository.save(qnaPostLike);
        }
    }

    @Transactional
    @Override
    public void toggleScrap(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Optional<QnaPostScrap> qnaPostScrapOptional = qnaPostScrapRepository.findByQnaPost_IdAndMember_Id(qnaPost.getId(), member.getId());

        if(qnaPostScrapOptional.isPresent()) {
            qnaPostScrapRepository.delete(qnaPostScrapOptional.get());
        } else {
            QnaPostScrap qnaPostScrap = QnaPostScrap.from(member, qnaPost);
            qnaPostScrapRepository.save(qnaPostScrap);
        }
    }

    public Long getLikeCount(Long qnaPostId) {
        return qnaPostRepository.getLikeCountByQnaPostId(qnaPostId);
    }
}
