package com.matching.plan.domain;

import com.matching.member.domain.Member;
import com.matching.post.domain.Post;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member participant;

    private boolean completed;

    public static Plan from(Member participant, Post post) {
        return Plan.builder()
                .post(post)
                .participant(participant)
                .completed(false)
                .build();
    }
}
