package com.matching.comment.service.impl;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.dto.CommentRequest;
import com.matching.comment.dto.CommentResponse;
import com.matching.comment.repository.QnaCommentRepository;
import com.matching.comment.service.CommentService;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaPost;
import com.matching.post.repository.QnaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final QnaPostRepository qnaPostRepository;
    private final MemberRepository memberRepository;
    private final QnaCommentRepository qnaCommentRepository;
    @Override
    public QnaComment createQnaComment(CommentRequest parameter, Long postId, Long memberId) {
        QnaPost qnaPost = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Qna 게시글을 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        QnaComment qnaComment = QnaComment.from(parameter, qnaPost, member);

        return qnaCommentRepository.save(qnaComment);
    }
}
