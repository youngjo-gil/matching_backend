package com.matching.post.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class QnaHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hashtag;

    @ManyToOne
    @JoinColumn(name = "qna_post_id")
    private QnaPost qnaPost;

    public static QnaHashtag from(String hashtag, QnaPost qnaPost) {
        return QnaHashtag.builder()
                .hashtag(hashtag)
                .qnaPost(qnaPost)
                .build();
    }
}
