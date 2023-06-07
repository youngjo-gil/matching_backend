package com.matching.member.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.MemberUpdateRequest;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseDto signup(
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFile,
            @RequestPart(value = "request") @Valid SignUpRequest parameter
    ) {
        boolean signUpComplete = memberService.signup(parameter, multipartFile);

        if(signUpComplete) {
            return ResponseUtil.SUCCESS("회원가입 완료", true);
        } else {
            return ResponseUtil.FAILURE("회원가입 실패", false);
        }
    }

    @PostMapping("/sign-in")
    public ResponseDto signIn(
            @RequestBody @Valid SignInRequest parameter
    ) {
        MemberResponse memberResponse = memberService.signIn(parameter);
        return ResponseUtil.SUCCESS("로그인 성공", memberResponse);
    }

    @PostMapping("/reissue")
    public ResponseDto reissue(
            HttpServletRequest request,
            @AuthenticationPrincipal User user
    ) {
        Long id = Long.parseLong(user.getUsername());
        MemberResponse memberResponse = memberService.reissue(request, id);

        return ResponseUtil.SUCCESS("토큰 재발행 성공", memberResponse);
    }

    @PatchMapping("/update")
    public ResponseDto updateMember(
            @RequestPart(value = "request") MemberUpdateRequest parameter,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFile,
            @AuthenticationPrincipal User user
    ) {
        Long id = Long.parseLong(user.getUsername());

        MemberResponse memberResponse = memberService.updateMember(parameter, id, multipartFile);

        return ResponseUtil.SUCCESS("회원정보 수정 완료", memberResponse);
    }


    @PostMapping("/logout")
    public ResponseDto logout(
            HttpServletRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseUtil.SUCCESS("로그아웃 완료", memberService.logout(request, Long.parseLong(user.getUsername())));
    }

    @PatchMapping("/withdraw")
    public ResponseDto withdraw(
            HttpServletRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseUtil.SUCCESS("회원 탈퇴 완료", memberService.withdraw(request, Long.parseLong(user.getUsername())));
    }
}
