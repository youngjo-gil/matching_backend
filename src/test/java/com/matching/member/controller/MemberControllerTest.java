//package com.matching.member.controller;
//
//import com.matching.member.dto.MemberResponse;
//import com.matching.member.dto.SignInRequest;
//import com.matching.member.dto.SignUpRequest;
//import com.matching.member.service.MemberService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.Assert;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class MemberControllerTest {
//    @Autowired
//    private MemberService memberService;
//
//
//    @DisplayName("회원가입 성공")
//    @Test
//    void signupSuccess() {
//        // 신규 회원가입
//        SignUpRequest request = SignUpRequest.builder()
//                .email("test@tes.com")
//                .password("1234")
//                .name("test")
//                .nickname("test_name")
//                .build();
//
//        boolean signUpComplete = memberService.signup(request);
//
//        Assert.isTrue(signUpComplete);
//    }
//
//    @DisplayName("회원가입 실패")
//    @Test
//    void signupExistMemberFailure() {
//        SignUpRequest request = SignUpRequest.builder()
//                .email("test3@test.com")
//                .password("1")
//                .build();
//
//        memberService.signup(request);
//
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            memberService.signup(request);
//        });
//    }
//
//    @DisplayName("로그인 성공")
//    @Test
//    void signIn() {
//        SignInRequest request = SignInRequest.builder()
//                .email("test@test.com")
//                .password("1234")
//                .build();
//
//        MemberResponse memberResponse = memberService.signIn(request);
//        assertEquals(memberResponse.getEmail(), "test@test.com");
//    }
//
//    @DisplayName("로그인 실패")
//    @Test
//    void signInPasswordError() {
//        SignInRequest request = SignInRequest.builder()
//                .email("test@test.com")
//                .password("1231")
//                .build();
//
//        Assertions.assertThrows(RuntimeException.class, () -> {
//            MemberResponse memberResponse = memberService.signIn(request);
//        });
//    }
//}