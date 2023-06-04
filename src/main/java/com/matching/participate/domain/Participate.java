package com.matching.participate.domain;

import com.matching.member.domain.Member;
import com.matching.post.domain.ProjectPost;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Participate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member participate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_post_id")
    private ProjectPost projectPost;

    @Enumerated(EnumType.STRING)
    private ParticipateStatus status;


    /**
     * BEFORE 승인전 , ADMISSION 참가 승인, REFUSE 참가 거부, EXIT 중도 포기, SUCCESS 성공, LEADER 팀장
     */
    public enum ParticipateStatus {
        BEFORE, ADMISSION, REFUSE, FAIL, SUCCESS, LEADER
    }

    public void updateStatus(ParticipateStatus status) {
        this.status = status;
    }


    public static Participate from(Member member, ProjectPost projectPost) {
        return Participate.builder()
                .participate(member)
                .projectPost(projectPost)
                .status(ParticipateStatus.BEFORE)
                .build();
    }

}
