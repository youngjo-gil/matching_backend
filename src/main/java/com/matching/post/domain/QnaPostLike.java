package com.matching.post.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
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
public class QnaPostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_post_id")
    private QnaPost qnaPost;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static QnaPostLike from(Member member, QnaPost qnaPost) {
         return QnaPostLike.builder()
                 .qnaPost(qnaPost)
                 .member(member)
                 .build();
    }
}
