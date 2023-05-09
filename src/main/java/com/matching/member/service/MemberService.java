package com.matching.member.service;

import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.dto.MemberUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    boolean signup(SignUpRequest parameter, MultipartFile multipartFile);
    MemberResponse signIn(SignInRequest parameter);
    MemberResponse updateMember(MemberUpdateRequest parameter, Long id, MultipartFile multipartFile);
}
