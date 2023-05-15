package com.matching.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
public class SignInRequest {
    @NotEmpty(message = "이메일 입력은 필수입니다.")
    private String email;

    @NotEmpty(message = "비밀번호 입력은 필수입니다.")
    private String password;
}
