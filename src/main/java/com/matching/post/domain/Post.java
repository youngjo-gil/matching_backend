package com.matching.post.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import com.matching.post.dto.PostRequest;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;

    // 글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member author;

    public static Post from(PostRequest parameter) {
        return Post.builder()
                .title(parameter.getTitle())
                .content(parameter.getContent())
                .author(parameter.getMember())
                .build();
    }
}
