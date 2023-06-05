package com.matching.post.dto;

import com.matching.comment.domain.QnaComment;
import com.matching.post.domain.QnaPost;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaPostResponse {
    private String title;
    private String body;
    private Long likeCount;
    private List<QnaComment> qnaCommentList;
    private LocalDateTime createdAt;

    public static QnaPostResponse from(QnaPost qnaPost, Long likeCount) {
        return QnaPostResponse.builder()
                .title(qnaPost.getTitle())
                .body(qnaPost.getBody())
                .likeCount(likeCount)
                .build();
    }
    public static QnaPostResponse from(QnaPost qnaPost, Long likeCount, List<QnaComment> qnaCommentList) {
        return QnaPostResponse.builder()
                .title(qnaPost.getTitle())
                .body(qnaPost.getBody())
                .qnaCommentList(qnaCommentList)
                .likeCount(likeCount)
                .build();
    }
}
