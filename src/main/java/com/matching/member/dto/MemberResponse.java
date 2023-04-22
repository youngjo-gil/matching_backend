package com.matching.member.dto;

import com.matching.member.domain.Member;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private String email;
    private String name;
    private String nickname;
    private String profileImageUrl;
    private String accessToken;

    public static MemberResponse of(Member member, String accessToken) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .accessToken(accessToken)
                .build();
    }
}
