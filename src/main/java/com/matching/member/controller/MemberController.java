package com.matching.member.controller;

import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.dto.MemberUpdateRequest;
import com.matching.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @RequestPart(value = "request") SignUpRequest parameter
    ) {
        boolean signUpComplete = memberService.signup(parameter, multipartFile);

        if(signUpComplete) {
            return ResponseEntity.ok().body("회원가입 완료");
        } else {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestBody SignInRequest parameter
    ) {
        MemberResponse memberResponse = memberService.signIn(parameter);

        return ResponseEntity.ok().body(memberResponse);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateMember(
            @RequestPart(value = "request") MemberUpdateRequest parameter,
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal User user
    ) {
        Long id = Long.parseLong(user.getUsername());

        MemberResponse memberResponse = memberService.updateMember(parameter, id, multipartFile);

        return ResponseEntity.ok().body(memberResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return null;
    }
}
