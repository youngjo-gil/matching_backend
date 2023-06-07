package com.matching.scrap.domain;

import com.matching.member.domain.Member;
import com.matching.post.domain.QnaPost;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class QnaPostScrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_post_id")
    private QnaPost qnaPost;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static QnaPostScrap from(Member member, QnaPost qnaPost) {
        return QnaPostScrap.builder()
                .member(member)
                .qnaPost(qnaPost)
                .build();
    }
}
