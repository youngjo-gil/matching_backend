package com.matching.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateRequest {
    private String password;
    private String nickname;
    private String profileImageUrl;
}
