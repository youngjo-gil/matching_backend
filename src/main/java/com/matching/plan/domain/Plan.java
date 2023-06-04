package com.matching.plan.domain;

import com.matching.member.domain.Member;
import com.matching.post.domain.ProjectPost;
import com.matching.post.dto.ProjectPostRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private ProjectPost projectPost;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member participant;

    public static Plan from(ProjectPostRequest parameter, Member participant, ProjectPost projectPost) {
        return Plan.builder()
                .projectPost(projectPost)
                .participant(participant)
                .detail(parameter.getDetail())
                .startedAt(parameter.getStartedAt())
                .endedAt(parameter.getEndedAt())
                .completed(false)
                .build();
    }
}
