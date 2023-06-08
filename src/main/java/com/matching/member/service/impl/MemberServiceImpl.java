package com.matching.member.service.impl;

import com.matching.MatchingApplication;
import com.matching.aws.service.AwsS3Service;
import com.matching.common.config.JwtTokenProvider;
import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.follow.domain.Follow;
import com.matching.follow.repository.FollowRepository;
import com.matching.member.domain.*;
import com.matching.member.dto.MemberResponse;
import com.matching.member.dto.MemberUpdateRequest;
import com.matching.member.dto.SignInRequest;
import com.matching.member.dto.SignUpRequest;
import com.matching.member.repository.MemberRepository;
import com.matching.member.repository.RefreshTokenRepository;
import com.matching.member.repository.SkillRepository;
import com.matching.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final SkillRepository skillRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AwsS3Service awsS3Service;

    private final FollowRepository followRepository;

    @Value("${jwt.refresh-token-expire-time}")
    int expiredTime;
    private final Logger logger = LoggerFactory.getLogger(MatchingApplication.class);

    /**
     * 회원 가입
     * @param Member
     * @return boolean
     */
    @Transactional
    @Override
    public boolean signup(SignUpRequest parameter, List<MultipartFile> multipartFile) {
        logger.info("sign up: " + parameter.getEmail());

        boolean existsByEmail = memberRepository.existsByEmail(parameter.getEmail());

        if(existsByEmail) {
            throw new CustomException(ErrorCode.USER_EXIST_EMAIL);
        }

        if(!multipartFile.isEmpty()) {
            List<String> uploadFile = awsS3Service.upload(multipartFile);
            parameter.setProfileImageUrl(uploadFile.get(0));
        }

        parameter.setPassword(passwordEncoder.encode(parameter.getPassword()));

        Member member = memberRepository.save(Member.from(parameter));

        return !ObjectUtils.isEmpty(member);
    }

    /**
     * 로그인
     * @param SignInRequest (email, password)
     * @return MemberResponse
     */
    @Override
    public MemberResponse signIn(SignInRequest parameter) {
        Member member = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(parameter.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRoles(member.getRole()));
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getRoles(member.getRole()));

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(String.valueOf(member.getId()))
                        .refreshToken(refreshToken)
                        .build()
        );

        return MemberResponse.of(member, accessToken);
    }

    @Override
    public MemberResponse reissue(HttpServletRequest request, Long id) {
        try{
            String accessToken = jwtTokenProvider.resolveToken(request);

            RefreshToken refreshToken = refreshTokenRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("리프레쉬 토큰이 없습니다."));

            if(jwtTokenProvider.validateToken(accessToken) == 2) {
                Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                String newAccessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRoles(member.getRole()));
                String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getId(), member.getRoles(member.getRole()));

                refreshTokenRepository.save(
                        RefreshToken.builder()
                                .userId(String.valueOf(member.getId()))
                                .refreshToken(newRefreshToken)
                                .build()
                );

                return MemberResponse.of(member, newAccessToken);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("토큰 만료");
        }
        return null;
    }


    /**
     * 회정정보 수정
     * @param UpdateMemberRequest
     * @return MemberResponse
     */
    @Transactional
    @Override
    public MemberResponse updateMember(MemberUpdateRequest parameter, Long id, List<MultipartFile> multipartFile) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        member.getMemberSkills().clear();

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRoles(member.getRole()));

        if(multipartFile != null) {
            List<String> uploadFile = awsS3Service.upload(multipartFile);
            parameter.setProfileImageUrl(uploadFile.get(0));
        }

        List<Long> memberSkillsId = parameter.getMemberSkillsId();

        if(memberSkillsId != null && !memberSkillsId.isEmpty()) {
            List<Skill> skills = skillRepository.findAllById(memberSkillsId);

            for (Skill skill: skills) {
                MemberSkill memberSkill = MemberSkill.from(skill, member);
                member.getMemberSkills().add(memberSkill);
            }
        }

        member.update(parameter);

        return MemberResponse.of(member, accessToken);
    }

    /**
     * 로그아웃
     * @param request
     * @param memberId
     * @return
     */
    @Override
    public boolean logout(HttpServletRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String token = jwtTokenProvider.resolveToken(request);

        updateUserByTokenBlackList(token, member.getId());

        logger.info("logout: " + member.getId());
        return true;
    }


    /**
     * 회원 탈퇴
     * @param request
     * @param memberId
     * @return
     */
    @Override
    public boolean withdraw(HttpServletRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String token = jwtTokenProvider.resolveToken(request);

        updateUserByTokenBlackList(token, member.getId());
        member.setStatus(MemberStatus.WITHDRAW);

        logger.info("withdraw: " + member.getId());

        return true;
    }

    @Transactional
    @Override
    public void follow(Long followerId, Long followingId) {
        Member followerMember = memberRepository.findById(followerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Member followingMember = memberRepository.findByIdAndStatus(followingId, MemberStatus.REGISTERED)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLLOWING_USER_NOT_FOUND));

        if(followerMember.getId().equals(followingMember.getId())) {
            throw new CustomException(ErrorCode.CANNOT_FOLLOWING_SELF);
        }

        followRepository.findByFollowingMemberAndFollowerMember(followingMember, followerMember)
                .ifPresentOrElse(
                        followRepository::delete,
                        () -> followRepository.save(Follow.builder()
                                .followerMember(followerMember)
                                .followingMember(followingMember)
                                .build())
                );

    }

    private void updateUserByTokenBlackList(String accessToken, Long memberId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 유저 토큰 정보가 없습니다."));

        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        refreshTokenRepository.setBlackList(accessToken, expiration);
        refreshTokenRepository.delete(refreshToken);
    }
}
