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
public class ProjectPostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private ProjectPost projectPost;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public static ProjectPostLike from(Member member, ProjectPost projectPost) {
        return ProjectPostLike.builder()
                .projectPost(projectPost)
                .member(member)
                .build();
    }
}
