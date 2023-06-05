package com.matching.comment.service.impl;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.dto.CommentRequest;
import com.matching.comment.repository.QnaCommentRepository;
import com.matching.comment.service.CommentService;
import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaPost;
import com.matching.post.repository.QnaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final QnaPostRepository qnaPostRepository;
    private final MemberRepository memberRepository;
    private final QnaCommentRepository qnaCommentRepository;
    @Override
    public QnaComment createQnaComment(CommentRequest parameter, Long postId, Long memberId) {
        QnaPost qnaPost = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        QnaComment qnaComment = QnaComment.from(parameter, qnaPost, member);

        return qnaCommentRepository.save(qnaComment);
    }

    @Override
    public QnaComment updateQnaComment(CommentRequest parameter, Long commentId, Long memberId) {
        QnaComment qnaComment = qnaCommentRepository.findByIdAndAuthor_Id(commentId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        qnaComment.setContent(parameter.getContent());
        return qnaComment;
    }

    @Override
    public void removeQnaComment(Long commentId, Long memberId) {
        QnaComment qnaComment = qnaCommentRepository.findByIdAndAuthor_Id(commentId, memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        qnaCommentRepository.delete(qnaComment);
    }
}
