package com.matching.post.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import com.matching.post.dto.QnaPostRequest;
import com.matching.scrap.domain.QnaPostScrap;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class QnaPost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member author;

    @OneToMany(mappedBy = "qnaPost", orphanRemoval = true)
    private List<QnaHashtag> hashtags = new ArrayList<>();
    @OneToMany(mappedBy = "qnaPost", orphanRemoval = true)
    private List<QnaPostLike> qnaPostLikes = new ArrayList<>();

    @OneToMany(mappedBy = "qnaPost", orphanRemoval = true)
    private List<QnaPostScrap> qnaPostScrapList = new ArrayList<>();


    public void update(QnaPostRequest request) {
        this.title = request.getTitle();
        this.body = request.getBody();
    }


    public static QnaPost from(QnaPostRequest parameter, Member member) {
        return QnaPost.builder()
                .title(parameter.getTitle())
                .body(parameter.getBody())
                .author(member)
                .build();
    }
}
