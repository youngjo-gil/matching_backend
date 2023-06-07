package com.matching.post.service.impl;

import com.matching.comment.domain.QnaComment;
import com.matching.comment.repository.QnaCommentRepository;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.domain.MemberRole;
import com.matching.member.domain.MemberStatus;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.QnaHashtag;
import com.matching.post.domain.QnaPost;
import com.matching.post.domain.QnaPostLike;
import com.matching.post.dto.QnaPostRequest;
import com.matching.post.dto.QnaPostResponse;
import com.matching.post.repository.QnaHashtagRepository;
import com.matching.post.repository.QnaPostLikeRepository;
import com.matching.post.repository.QnaPostRepository;
import com.matching.post.service.QnaHashtagService;
import com.matching.scrap.domain.QnaPostScrap;
import com.matching.scrap.repostory.QnaPostScrapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QnaPostServiceImplTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private QnaHashtagRepository qnaHashtagRepository;
    @Mock
    private QnaPostLikeRepository qnaPostLikeRepository;
    @Mock
    private QnaCommentRepository qnaCommentRepository;
    @Mock
    private QnaPostScrapRepository qnaPostScrapRepository;
    @Mock
    private QnaPostRepository qnaPostRepository;
    @Mock
    private QnaHashtagService qnaHashtagService;
    @InjectMocks
    private QnaPostServiceImpl qnaPostService;

    Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("1111")
            .name("test")
            .status(MemberStatus.REGISTERED)
            .role(MemberRole.USER)
            .build();
    @Test
    @DisplayName("[QnaPost] 글쓰기 성공")
    void successWriteQna() {
        // given
        QnaPostRequest request = QnaPostRequest.builder()
                .title("qna 테스트")
                .body("qna 테스트입니다.")
                .hashtagList(Arrays.asList("해시태그1", "해시태그2"))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        
        given(qnaPostRepository.save(any()))
                .willReturn(QnaPost.builder()
                        .id(1L)
                        .title("qna 테스트")
                        .body("qna 테스트 입니다.")
                        .author(member)
                        .qnaPostLikes(null)
                        .qnaPostScrapList(null)
                        .build()
                );
        // when
        qnaHashtagService.saveQnaHashtag(any(), any());

        qnaPostService.writeQna(request, 1L);
        ArgumentCaptor<QnaPost> captor = ArgumentCaptor.forClass(QnaPost.class);
        // then
        verify(qnaPostRepository, times(1)).save(captor.capture());

        assertEquals("qna 테스트", captor.getValue().getTitle());
    }

    @Test
    @DisplayName("[QnaPost] 글쓰기 실패 - 회원없음")
    void failureWriteQnaMemberNotFound() {
        QnaPostRequest request = QnaPostRequest.builder()
                .title("qna 테스트")
                .body("qna 테스트입니다.")
                .hashtagList(Arrays.asList("해시태그1", "해시태그2"))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(CustomException.class, () -> qnaPostService.writeQna(request, 1L));
    }

    @Test
    @DisplayName("[QnaPost] 글쓰기 저장 실패")
    void failureWritQna() {
        QnaPostRequest request = QnaPostRequest.builder()
                .title("qna 테스트")
                .body("qna 테스트입니다.")
                .hashtagList(Arrays.asList("해시태그1", "해시태그2"))
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(qnaPostRepository.save(any(QnaPost.class))).willThrow(CustomException.class);

        assertThrows(CustomException.class, () -> {
            qnaPostService.writeQna(request, 1L);
        });
    }

    @Test
    @DisplayName("[QnaPost] 조회 성공")
    void successGetQna() {
        // given
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        List<QnaComment> qnaCommentList = List.of(
                QnaComment.builder()
                        .id(1L)
                        .qnaPost(qnaPost)
                        .author(member)
                        .content("댓글")
                        .build()
        );
        given(qnaPostRepository.findById(anyLong()))
                .willReturn(Optional.of(qnaPost));
        given(qnaCommentRepository.findAllByQnaPost_Id(anyLong()))
                .willReturn(Optional.of(qnaCommentList));
        // when
        QnaPostResponse response = qnaPostService.getQna(1L);
        // then
        assertEquals("qna 테스트", response.getTitle());
    }

    @Test
    @DisplayName("[QnaPost] 수정 성공")
    void successUpdateQna() {
        QnaPostRequest request = QnaPostRequest.builder()
                .title("qna 테스트 수정 수정")
                .body("qna 테스트입니다.")
                .hashtagList(Arrays.asList("해시태그1", "해시태그2"))
                .build();

        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        QnaHashtag hashtag = QnaHashtag.builder()
                .id(1L)
                .qnaPost(qnaPost)
                .hashtag("해시태그1")
                .build();

        List<QnaHashtag> hashtagList = new ArrayList<>();
        hashtagList.add(hashtag);
        qnaPost.setHashtags(hashtagList);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(qnaPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(qnaPost));

        // then
        qnaPostService.updateQna(request, 1L, 1L);

        assertEquals("qna 테스트 수정 수정", qnaPost.getTitle());
    }

    @Test
    @DisplayName("[QnaPost] 삭제 성공")
    void successDeleteQna() {
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(qnaPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(qnaPost));

        Long postId = qnaPostService.deleteQna(1L, 1L);

        assertEquals(1L, postId);
    }

    @Test
    @DisplayName("[QnaPost] 삭제 실패")
    void failureDeleteQna() {
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(qnaPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong())).willReturn(Optional.of(qnaPost));
        doThrow(CustomException.class).when(qnaPostRepository).delete(any(QnaPost.class));

        assertThrows(CustomException.class, () -> {
            qnaPostService.deleteQna(1L, 1L);
        });
    }

    @Test
    @DisplayName("[QnaPost] 좋아요 성공")
    void toggleLike() {
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        QnaPostLike qnaPostLike = QnaPostLike.builder()
                .id(1L)
                .qnaPost(qnaPost)
                .member(member)
                .build();

        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(qnaPostRepository.findById(anyLong()))
                .willReturn(Optional.of(qnaPost));
        given(qnaPostLikeRepository.findByMember_IdAndQnaPost_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(qnaPostLike));

        // when
        qnaPostService.toggleLike(anyLong(), anyLong());

        // then
        verify(qnaPostLikeRepository, times(1)).delete(qnaPostLike);
        verify(qnaPostLikeRepository, times(0)).save(any(QnaPostLike.class));
    }

    @Test
    @DisplayName("[QnaPost] 좋아요 실패")
    void failToToggleLike() {
        // given
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(qnaPostRepository.findById(anyLong())).willReturn(Optional.of(qnaPost));
        given(qnaPostLikeRepository.findByMember_IdAndQnaPost_Id(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        qnaPostService.toggleLike(anyLong(), anyLong());

        // then
        verify(qnaPostLikeRepository, never()).delete(any(QnaPostLike.class));
    }

    @Test
    @DisplayName("[QnaPost] 스크랩 성공")
    void successToggleScrap() {
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        QnaPostScrap qnaPostScrap = QnaPostScrap.builder()
                .id(1L)
                .qnaPost(qnaPost)
                .member(member)
                .build();

        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(qnaPostRepository.findById(anyLong()))
                .willReturn(Optional.of(qnaPost));
        given(qnaPostScrapRepository.findByQnaPost_IdAndMember_Id(anyLong(), anyLong()))
                .willReturn(Optional.of(qnaPostScrap));
        // when
        qnaPostService.toggleScrap(1L, 1L);

        // then
        verify(qnaPostScrapRepository, times(1)).delete(qnaPostScrap);
    }

    @Test
    @DisplayName("[QnaPost] 스크랩 실패")
    void failureToggleScrap() {
        // given
        QnaPost qnaPost = QnaPost.builder()
                .id(1L)
                .title("qna 테스트")
                .body("qna 테스트 입니다.")
                .author(member)
                .qnaPostLikes(null)
                .qnaPostScrapList(null)
                .build();

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(qnaPostRepository.findById(anyLong())).willReturn(Optional.of(qnaPost));
        given(qnaPostScrapRepository.findByQnaPost_IdAndMember_Id(anyLong(), anyLong())).willReturn(Optional.empty());

        // when
        qnaPostService.toggleScrap(1L, 1L);

        // then
        verify(qnaPostScrapRepository, never()).delete(any(QnaPostScrap.class));
    }

    @Test
    @DisplayName("[QnaPost] 스크랩 게시글 조회 성공")
    void successGetPostByScrap() {
        int pageNum = 0;
        QnaPost qnaPost1 = QnaPost.builder()
                .id(1L)
                .title("qna 테스트 1")
                .body("qna 테스트 입니다.")
                .author(member)
                .build();
        QnaPost qnaPost2 = QnaPost.builder()
                .id(2L)
                .title("qna 테스트 2")
                .body("qna 테스트 입니다.")
                .author(member)
                .build();

        List<QnaPost> qnaPostList = Arrays.asList(qnaPost1, qnaPost2);
        Page<QnaPost> qnaPostPage = new PageImpl<>(qnaPostList);

        // given
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(qnaPostRepository.findAllQnaPostByScrapMemberId(anyLong(), any(Pageable.class)))
                .willReturn(qnaPostPage);
        // when
        Page<QnaPostResponse> result = qnaPostService.getPostByScrap(member.getId(), pageNum);

        // then
        assertEquals(qnaPostPage.getTotalElements(), result.getTotalElements());
        assertEquals(qnaPostList.size(), result.getContent().size());
        assertEquals(qnaPostList.get(0).getId(), result.getContent().get(0).getId());
        assertEquals(qnaPostList.get(1).getId(), result.getContent().get(1).getId());
    }
}