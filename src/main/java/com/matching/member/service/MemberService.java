package com.matching.member.service;

import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;

public interface MemberService {
    boolean signup(SignUpRequest parameter);
    MemberResponse signIn(SignInRequest parameter);
}
