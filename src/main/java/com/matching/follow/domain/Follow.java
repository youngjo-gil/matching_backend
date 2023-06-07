package com.matching.follow.domain;

import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AuditOverride(forClass = BaseEntity.class)
public class Follow extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member followingId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member followerId;
}
