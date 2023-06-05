package com.matching.comment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matching.comment.dto.CommentRequest;
import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import com.matching.post.domain.QnaPost;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class QnaComment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_post_id")
    @JsonIgnore
    private QnaPost qnaPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private Member author;

    public static QnaComment from(CommentRequest parameter, QnaPost qnaPost, Member member) {
        return QnaComment.builder()
                .content(parameter.getContent())
                .qnaPost(qnaPost)
                .author(member)
                .build();

    }
}
