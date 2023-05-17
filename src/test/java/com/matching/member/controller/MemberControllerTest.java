package com.matching.member.controller;

import com.matching.common.config.JwtTokenProvider;
import com.matching.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc
class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private static String token = "bearer token";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("회원가입 성공")
    void successSignup() throws Exception {
        // given
        given(memberService.signup(any(), any())).willReturn(true);
        // when

        // then

    }

}