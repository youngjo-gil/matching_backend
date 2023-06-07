package com.matching.post.service.impl;

import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.domain.MemberRole;
import com.matching.member.domain.MemberStatus;
import com.matching.member.repository.MemberRepository;
import com.matching.participate.domain.Participate;
import com.matching.participate.repository.ParticipateRepository;
import com.matching.photo.domain.Photo;
import com.matching.photo.repository.PhotoRepository;
import com.matching.plan.domain.Plan;
import com.matching.plan.repository.PlanRepository;
import com.matching.post.domain.Category;
import com.matching.post.domain.ProjectPost;
import com.matching.post.dto.PostSearchRequest;
import com.matching.post.dto.ProjectPostRequest;
import com.matching.post.dto.ProjectPostResponse;
import com.matching.post.dto.ProjectPostUpdateRequest;
import com.matching.post.repository.CategoryRepository;
import com.matching.post.repository.PostRepositoryQuerydsl;
import com.matching.post.repository.ProjectPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectPostServiceImplTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProjectPostRepository projectPostRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private ParticipateRepository participateRepository;
    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private PostRepositoryQuerydsl postRepositoryQuerydsl;

    @InjectMocks
    private ProjectPostServiceImpl projectPostService;

    Member member = Member.builder()
            .id(1L)
            .email("test@test.com")
            .password("1111")
            .name("test")
            .status(MemberStatus.REGISTERED)
            .role(MemberRole.USER)
            .build();
    Category category = Category.builder()
            .id(1L)
            .build();

    Plan plan = Plan.builder()
            .id(1L)
            .build();

    ProjectPost projectPost = ProjectPost.builder()
            .id(1L)
            .title("스터디 모집")
            .body("백엔드 스터디")
            .author(member)
            .plan(plan)
            .category(category)
            .build();

    @Test
    @DisplayName("[ProjectPost] 글쓰기 성공")
    void successWritePost() {
        ProjectPostRequest parameter = ProjectPostRequest.builder()
                .title("스터디 모집")
                .body("백엔드 스터디")
                .summary("공부합니다.")
                .member(member)
                .categoryId(1L)
                .planBody("우리 함께하는거야")
                .startedAt(LocalDate.now())
                .endedAt(LocalDate.now())
                .build();
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(category));
        given(projectPostRepository.save(any())).willReturn(ProjectPost.builder().id(1L).build());
        given(planRepository.save(any())).willReturn(Plan.builder().id(1L).build());
        given(participateRepository.save(any())).willReturn(Participate.builder().id(1L).build());
        // when
        Long postId = projectPostService.writePost(parameter, "1", null);

        // then
        assertNotNull(postId);
        verify(memberRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(projectPostRepository, times(1)).save(any(ProjectPost.class));
        verify(planRepository, times(1)).save(any(Plan.class));
        verify(participateRepository, times(1)).save(any(Participate.class));
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 조회 성공")
    void successGetPost() {
        Photo photo = Photo.builder()
                .id(1L)
                .projectPost(projectPost)
                .pathname("test.png")
                .createdAt(LocalDateTime.now())
                .build();
        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo);

        // given
        given(projectPostRepository.findById(anyLong())).willReturn(Optional.of(projectPost));
        given(photoRepository.findAllByProjectPost_Id(anyLong())).willReturn(Optional.of(photoList));

        // when
        ProjectPostResponse response = projectPostService.getPost(1L);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 검색 목록 조회 성공")
    void getPostSearchList() {
        // given
        PostSearchRequest parameter = PostSearchRequest.builder()
                .keyword("키워드")
                .pageNum(0)
                .pageSize(5)
                .build();

        Photo photo = Photo.builder()
                .id(1L)
                .projectPost(projectPost)
                .pathname("test.png")
                .createdAt(LocalDateTime.now())
                .build();
        projectPost.setPhotoList(List.of(photo));
        Page<ProjectPost> postPage = new PageImpl<>(Collections.singletonList(projectPost));

        given(postRepositoryQuerydsl.findAll(any(), anyString())).willReturn(postPage);

        // when
        Page<ProjectPostResponse> responsePage = projectPostService.getPostSearchList(parameter);

        // then
        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        verify(postRepositoryQuerydsl, times(1)).findAll(any(), anyString());
    }

    @Test
    @DisplayName("[ProjectPost] 참가중인 게시물 조회 성공")
    void successGetPostByParticipant() {
        Photo photo = Photo.builder()
                .id(1L)
                .projectPost(projectPost)
                .pathname("test.png")
                .createdAt(LocalDateTime.now())
                .build();
        projectPost.setPhotoList(List.of(photo));

        Page<ProjectPost> postPage = new PageImpl<>(Collections.singletonList(projectPost));

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(projectPostRepository.findAllOrderByParticipateByPhotoCreatedAtDesc(anyLong(), any())).willReturn(postPage);

        // when
        Page<ProjectPostResponse> responsePage = projectPostService.getPostByParticipant(1L);

        // then
        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(projectPostRepository, times(1)).findAllOrderByParticipateByPhotoCreatedAtDesc(anyLong(), any());
    }

    @Test
    @DisplayName("[ProjectPost] 작성한 글 조회 성공")
    void successGetPostByWrite() {
        Photo photo = Photo.builder()
                .id(1L)
                .projectPost(projectPost)
                .pathname("test.png")
                .createdAt(LocalDateTime.now())
                .build();
        projectPost.setPhotoList(List.of(photo));


        Page<ProjectPost> postPage = new PageImpl<>(Collections.singletonList(projectPost));

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(projectPostRepository.findAllByAuthor_Id(anyLong(), any())).willReturn(postPage);

        // when
        Page<ProjectPostResponse> responsePage = projectPostService.getPostByWrite(1L);

        // then
        assertNotNull(responsePage);
        assertFalse(responsePage.isEmpty());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(projectPostRepository, times(1)).findAllByAuthor_Id(anyLong(), any());
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 수정 성공")
    void updatePost() {
        // given
        ProjectPostUpdateRequest parameter = ProjectPostUpdateRequest.builder()
                .title("사이드 프로젝트")
                .body("웹개발")
                .build();
        Photo photo = Photo.builder()
                .id(1L)
                .projectPost(projectPost)
                .pathname("test.png")
                .createdAt(LocalDateTime.now())
                .build();
        projectPost.setPhotoList(List.of(photo));


        given(projectPostRepository.findByIdAndAuthor_Id(anyLong(), anyLong())).willReturn(Optional.of(projectPost));

        // when
        Long updatedPostId = projectPostService.updatePost(1L, 1L, parameter);

        // then
        assertNotNull(updatedPostId);
        assertEquals(1L, updatedPostId);
        verify(projectPostRepository, times(1)).findByIdAndAuthor_Id(anyLong(), anyLong());
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 삭제 성공")
    void successDeletePost() {
        // given
        Participate participate = Participate.builder()
                .status(Participate.ParticipateStatus.LEADER)
                .build();

        given(projectPostRepository.findById(anyLong())).willReturn(Optional.of(projectPost));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(participateRepository.findByParticipate_IdAndProjectPost_Id(anyLong(), anyLong())).willReturn(Optional.of(participate));

        // when
        assertDoesNotThrow(() -> projectPostService.deletePost(1L, 1L));

        // then
        verify(projectPostRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(participateRepository, times(1)).findByParticipate_IdAndProjectPost_Id(anyLong(), anyLong());
        verify(projectPostRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 삭제 실패 - 참가자 리더 아님")
    void failureDeletePostNotLeader() {
        // given

        Participate participate = Participate.builder()
                .status(Participate.ParticipateStatus.SUCCESS)
                .build();

        given(projectPostRepository.findById(anyLong())).willReturn(Optional.of(projectPost));
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(participateRepository.findByParticipate_IdAndProjectPost_Id(anyLong(), anyLong())).willReturn(Optional.of(participate));

        // when & then
        assertThrows(CustomException.class, () -> projectPostService.deletePost(1L, 1L));

        verify(projectPostRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(participateRepository, times(1)).findByParticipate_IdAndProjectPost_Id(anyLong(), anyLong());
        verify(projectPostRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("[ProjectPost] 게시물 삭제 실패 - 사용자가 존재하지 않음")
    void failDeletePostUserNotFound() {
        // given
        given(projectPostRepository.findById(anyLong())).willReturn(Optional.of(projectPost));
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThrows(CustomException.class, () -> projectPostService.deletePost(1L, 1L));

        verify(projectPostRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).findById(anyLong());
        verify(participateRepository, never()).findByParticipate_IdAndProjectPost_Id(anyLong(), anyLong());
        verify(projectPostRepository, never()).deleteById(anyLong());
    }
}