package com.matching.plan.domain;

import com.matching.member.domain.Member;
import com.matching.plan.dto.PlanRequest;
import com.matching.post.domain.Post;
import com.matching.post.dto.PostRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

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
    private String detail;
    private boolean completed;

    private LocalDate startedAt;
    private LocalDate endedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member participant;

    public static Plan from(PostRequest parameter, Member participant, Post post) {
        return Plan.builder()
                .post(post)
                .participant(participant)
                .detail(parameter.getDetail())
                .startedAt(parameter.getStartedAt())
                .endedAt(parameter.getEndedAt())
                .completed(false)
                .build();
    }
}
