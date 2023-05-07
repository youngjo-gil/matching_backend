package com.matching.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMemberRequest {
    private String password;
    private String nickname;
    private String profileImageUrl;
}
