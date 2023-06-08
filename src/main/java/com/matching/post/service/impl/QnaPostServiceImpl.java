package com.matching.post.service.impl;

import com.matching.MatchingApplication;
import com.matching.comment.domain.QnaComment;
import com.matching.comment.repository.QnaCommentRepository;
import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaPost;
import com.matching.post.domain.QnaPostLike;
import com.matching.post.dto.ProjectPostResponse;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;
import com.matching.post.repository.QnaPostLikeRepository;
import com.matching.post.repository.QnaPostRepository;
import com.matching.post.service.QnaHashtagService;
import com.matching.post.service.QnaPostService;
import com.matching.scrap.domain.QnaPostScrap;
import com.matching.scrap.repostory.QnaPostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final Logger logger = LoggerFactory.getLogger(MatchingApplication.class);

    private final static int PAGE_SIZE = 10;

    @Transactional
    @Override
    public Long writeQna(QnaPostRequest parameter, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        QnaPost qnaPost = qnaPostRepository.save(QnaPost.from(parameter, member));

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        logger.info("QnaPost written by user: " + member.getId());

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

        qnaPost.setTitle(parameter.getTitle());
        qnaPost.setBody(parameter.getBody());

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        logger.info("QnaPost updated by user: " + memberId);

        return qnaPost.getId();
    }

    @Transactional
    @Override
    public Long deleteQna(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        qnaPostRepository.delete(qnaPost);

        logger.info("QnaPost deleted by user: " + memberId);

        return qnaPostId;
    }

    @Transactional
    @Override
    public void toggleLike(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        qnaPostLikeRepository.findByMember_IdAndQnaPost_Id(memberId, qnaPostId)
                .ifPresentOrElse(
                        qnaPostLikeRepository::delete,
                        () -> qnaPostLikeRepository.save(QnaPostLike.from(member, qnaPost))
                );
    }

    @Transactional
    @Override
    public void toggleScrap(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        qnaPostScrapRepository.findByQnaPost_IdAndMember_Id(qnaPostId, memberId)
                .ifPresentOrElse(
                        qnaPostScrapRepository::delete,
                        () -> qnaPostScrapRepository.save(QnaPostScrap.from(member, qnaPost))
                );
    }

    @Override
    public Page<QnaPostResponse> getPostByScrap(Long memberId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Page<QnaPost> qnaPostPage = qnaPostRepository.findAllQnaPostByScrapMemberId(member.getId(), pageable);

        return qnaPostPage.map(post -> {
            Long likeCount = getLikeCount(post.getId());
            return QnaPostResponse.from(post, likeCount);
        });
    }

    public Long getLikeCount(Long qnaPostId) {
        return qnaPostRepository.getLikeCountByQnaPostId(qnaPostId);
    }
}
