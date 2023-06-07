package com.matching.post.service.impl;

import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.domain.MemberRole;
import com.matching.member.domain.MemberStatus;
import com.matching.post.domain.QnaPost;
import com.matching.post.repository.QnaHashtagRepository;
import com.matching.post.service.QnaHashtagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class QnaHashtagServiceImplTest {
    @Mock
    private QnaHashtagRepository qnaHashtagRepository;
    @InjectMocks
    private QnaHashtagServiceImpl qnaHashtagService;

    Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("1111")
            .name("test")
            .status(MemberStatus.REGISTERED)
            .role(MemberRole.USER)
            .build();

    @Test
    @DisplayName("[QnaPost] 해시태그 저장 성공")
    void successSaveQnaHashtag() {

        // given
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        List<String> qnaHashtagList = Arrays.asList("tag1", "tag2", "ta3");

        given(qnaHashtagRepository.saveAll(anyIterable()))
                .willReturn(Collections.emptyList());

        // when
        qnaHashtagService.saveQnaHashtag(qnaPost, qnaHashtagList);

        // then
        verify(qnaHashtagRepository,times(1)).saveAll(anyIterable());
    }

    @Test
    @DisplayName("[QnaPost] 해시태그 저장 실패")
    void failureSaveQnaHashtag() {
        // given
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();
        List<String> qnaHashtagList = Arrays.asList("tag1", "tag2", "ta3");

        given(qnaHashtagRepository.saveAll(anyList()))
                .willThrow(CustomException.class);

        // when & then
        assertThrows(CustomException.class, () -> {
            qnaHashtagService.saveQnaHashtag(qnaPost, qnaHashtagList);
        });

        verify(qnaHashtagRepository, times(1)).saveAll(anyList());

    }
}