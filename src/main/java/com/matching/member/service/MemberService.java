package com.matching.member.service;

import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.dto.MemberUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MemberService {
    boolean signup(SignUpRequest parameter, List<MultipartFile> multipartFile);
    MemberResponse signIn(SignInRequest parameter);
    MemberResponse updateMember(MemberUpdateRequest parameter, Long id, List<MultipartFile> multipartFile);
    MemberResponse reissue(HttpServletRequest request, Long id);
    boolean logout(HttpServletRequest request, Long memberId);
    boolean withdraw(HttpServletRequest request, Long memberId);
}
