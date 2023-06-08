package com.matching.follow.repository;

import com.matching.follow.domain.Follow;
import com.matching.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingMemberAndFollowerMember(Member followingMember, Member followerMember);
}
