package com.matching.post.service.impl;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.repository.QnaCommentRepository;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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

    @Override
    public QnaPostResponse getQna(Long qnaPostId) {
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));
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
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        qnaPost.getHashtags().clear();

        qnaHashtagService.saveQnaHashtag(qnaPost, parameter.getHashtagList());

        return qnaPost.getId();
    }

    @Override
    public void deleteQna(Long memberId, Long qnaPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        QnaPost qnaPost = qnaPostRepository.findByIdAndAuthor_Id(qnaPostId, member.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        qnaPostRepository.delete(qnaPost);
    }

    @Override
    public void toggleLike(Long memberId, Long qnaPostId) {
        Optional<QnaPostLike> qnaPostLikeOptional = qnaPostLikeRepository.findByMember_IdAndQnaPost_Id(memberId, qnaPostId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        QnaPost qnaPost = qnaPostRepository.findById(qnaPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        if(qnaPostLikeOptional.isPresent()){
            // 좋아요가 있는경우 취소처리
            qnaPostLikeRepository.delete(qnaPostLikeOptional.get());
        } else {
            QnaPostLike qnaPostLike = QnaPostLike.from(member, qnaPost);
            qnaPostLikeRepository.save(qnaPostLike);
        }
    }

    public Long getLikeCount(Long qnaPostId) {
        return qnaPostRepository.getLikeCountByQnaPostId(qnaPostId);
    }
}
