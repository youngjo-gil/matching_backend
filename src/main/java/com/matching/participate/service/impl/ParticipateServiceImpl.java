package com.matching.participate.service.impl;

import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.participate.domain.Participate;
import com.matching.participate.dto.ParticipateStatusRequest;
import com.matching.participate.repository.ParticipateRepository;
import com.matching.participate.service.ParticipateService;
import com.matching.post.domain.ProjectPost;
import com.matching.post.repository.ProjectPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipateServiceImpl implements ParticipateService {
    private final ParticipateRepository participateRepository;
    private final MemberRepository memberRepository;
    private final ProjectPostRepository projectPostRepository;

    @Transactional
    @Override
    public Long insertParticipate(Long id, Long postId) {
        Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(postId)
                        .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return participateRepository.save(Participate.from(member, projectPost)).getId();
    }

    @Transactional
    @Override
    public Long updateParticipateStatus(Long id, Long postId, ParticipateStatusRequest parameter) {
        ProjectPost projectPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        Member member = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if(!projectPost.getAuthor().getId().equals(id)) {
            throw new CustomException(ErrorCode.PARTICIPATE_NOT_LEADER);
        }

        Participate participate = participateRepository.findByParticipate_IdAndProjectPost_Id(member.getId(), projectPost.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPATE_NOT_FOUND));

        participate.updateStatus(Participate.ParticipateStatus.ADMISSION);
        participateRepository.save(participate);

        return null;
    }
}
