package com.matching.member.service.impl;

import com.matching.common.config.JwtTokenProvider;
import com.matching.member.domain.Member;
import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.repository.MemberRepository;
import com.matching.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입
     * @param Member
     * @return boolean
     */
    @Override
    public boolean signup(SignUpRequest parameter) {
        boolean existsByEmail = memberRepository.existsByEmail(parameter.getEmail());

        if(existsByEmail) {
            throw new RuntimeException("회원 이메일이 존재합니다.");
        }

        parameter.setPassword(passwordEncoder.encode(parameter.getPassword()));

        Member member = memberRepository.save(Member.from(parameter));

        if(ObjectUtils.isEmpty(member)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 로그인
     * @param SignInRequest (email, password)
     * @return MemberResponse
     */
    @Override
    public MemberResponse signIn(SignInRequest parameter) {
        Member member = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 회원이 없습니다."));

        if(!passwordEncoder.matches(parameter.getPassword(), member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getEmail(), member.getRole());

        return MemberResponse.of(member, accessToken);
    }

}
