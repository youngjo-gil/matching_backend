package com.matching.member.controller;

import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
@CrossOrigin(origins = "*")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody SignUpRequest parameter
    ) {
        boolean signUpComplete = memberService.signup(parameter);

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
}
