package com.matching.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matching.common.config.JwtTokenProvider;
import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.MemberUpdateRequest;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.repository.MemberRepository;
import com.matching.member.service.MemberService;
import com.matching.util.WithMockCustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {
    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberService memberService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static String TOKEN = "bearer token";

    @BeforeEach
    void init(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("[Member] [Signup] 회원가입 성공")
    void successSignup() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test_test@test.com")
                .password("1234")
                .name("test_name")
                .nickname("nickname")
                .build();

        String content = objectMapper.writeValueAsString(request);

        List<MockMultipartFile> images = new ArrayList<>();

        images.add(new MockMultipartFile("file", "test.png",  "image/png",
                "<<png data>>".getBytes()));

        // when
        given(memberService.signup(any(), any()))
                .willReturn(true);

        // then
        mockMvc
            .perform(
                multipart("/api/v1/member/signup")
                        .file(images.get(0))
                        .file(new MockMultipartFile(
                                "request",
                                "",
                                "application/json",
                                content.getBytes(StandardCharsets.UTF_8)
                        ))

            )
            .andDo(print())
            .andExpect(status().isOk());
    }


    @Test
    @DisplayName("[Member] [SignIn] 로그인 성공")
    void successSignIn() throws Exception {
        // given
        SignInRequest request = SignInRequest.builder()
                .email("test_@test.com")
                .password("1111")
                .build();

        given(memberService.signIn(any()))
                .willReturn(new MemberResponse());

        // when
        String content = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(post("/api/v1/member/sign-in")
                        .with(csrf())
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andDo(print());
    }

    @Test
    @DisplayName("[Member] [Update] 회원정보 수정 성공")
    @WithMockCustomUser
    void successMemberUpdate() throws Exception {
        // given
        MemberUpdateRequest request = MemberUpdateRequest.builder()
                .password("3333")
                .nickname("test_name")
                .build();

        String content = objectMapper.writeValueAsString(request);

        List<MockMultipartFile> images = new ArrayList<>();

        images.add(new MockMultipartFile("file", "test.png",  "image/png",
                "<<png data>>".getBytes()));
        // when
        given(memberService.updateMember(any(), anyLong(), any()))
                .willReturn(new MemberResponse());

        // then
        mockMvc
                .perform(
                        multipart(HttpMethod.PATCH, "/api/v1/member/update")
                                .file(images.get(0))
                                .file(new MockMultipartFile(
                                        "request",
                                        "",
                                        "application/json",
                                        content.getBytes(StandardCharsets.UTF_8)
                                ))
//                                .header("Authorization", TOKEN)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}