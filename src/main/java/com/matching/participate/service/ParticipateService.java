package com.matching.participate.service;

import com.matching.participate.dto.ParticipateStatusRequest;

public interface ParticipateService {
    Long insertParticipate(Long id, Long postId);
    Long updateParticipateStatus(Long id, Long postId, ParticipateStatusRequest parameter);
}
