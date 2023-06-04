package com.matching.post.dto;

import com.matching.post.domain.QnaPost;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnaPostResponse {
    private String title;
    private String body;
    private Long likeCount;
    private LocalDateTime createdAt;

    public static QnaPostResponse from(QnaPost qnaPost, Long likeCount) {
        return QnaPostResponse.builder()
                .title(qnaPost.getTitle())
                .body(qnaPost.getBody())
                .likeCount(likeCount)
                .build();
    }
}
