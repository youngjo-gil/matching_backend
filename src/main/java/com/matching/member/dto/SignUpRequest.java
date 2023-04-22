package com.matching.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequest {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImageUrl;
}
