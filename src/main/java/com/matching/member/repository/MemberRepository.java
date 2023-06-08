package com.matching.member.repository;

import com.matching.member.domain.Member;
import com.matching.member.domain.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByIdAndStatus(Long memberId, MemberStatus memberStatus);
}
