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
    private String status;


    /**
     * ADMISSION 참가 승인, REFUSE 참가 거부, EXIT 중도 포기, SUCCESS 성공
     */
    public enum ParticipateStatus {
        ADMISSION, REFUSE, EXIT, SUCCESS
    }

}
