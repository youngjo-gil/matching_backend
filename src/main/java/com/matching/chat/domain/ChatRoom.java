package com.matching.chat.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.post.domain.ProjectPost;
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
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ProjectPost projectPost;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

}
