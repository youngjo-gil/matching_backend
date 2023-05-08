package com.matching.participate.domain;

import com.matching.member.domain.Member;
import com.matching.post.domain.Post;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private ParticipateStatus status;


    /**
     * BEFORE 승인전 , ADMISSION 참가 승인, REFUSE 참가 거부, EXIT 중도 포기, SUCCESS 성공
     */
    public enum ParticipateStatus {
        BEFORE, ADMISSION, REFUSE, FAIL, SUCCESS
    }

    public void updateStatus(ParticipateStatus status) {
        this.status = status;
    }


    public static Participate from(Member member, Post post) {
        return Participate.builder()
                .participate(member)
                .post(post)
                .status(ParticipateStatus.BEFORE)
                .build();
    }

}
