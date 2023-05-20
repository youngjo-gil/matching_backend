package com.matching.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matching.common.config.JwtTokenProvider;
import com.matching.member.domain.Member;
import com.matching.member.domain.MemberStatus;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.repository.MemberRepository;
import com.matching.member.service.MemberService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    private static String token = "bearer token";

    @BeforeEach
    void init(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void successSignup() throws Exception {
        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test_test@test.com")
                .password("1234")
                .name("test_name")
                .nickname("nickname")
                .build();

        String body = objectMapper.writeValueAsString(request);

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
                                body.getBytes(StandardCharsets.UTF_8)
                        ))

            )
            .andExpect(status().isOk());
    }

}