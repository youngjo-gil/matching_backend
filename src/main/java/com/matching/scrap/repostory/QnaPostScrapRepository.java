package com.matching.scrap.repostory;

import com.matching.scrap.domain.QnaPostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaPostScrapRepository extends JpaRepository<QnaPostScrap, Long> {
    Optional<QnaPostScrap> findByQnaPost_IdAndMember_Id(Long qnaPostId, Long memberId);
}
