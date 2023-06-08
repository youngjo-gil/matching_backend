package com.matching.post.service.impl;

import com.matching.exception.dto.ErrorCode;
import com.matching.exception.util.CustomException;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.participate.domain.Participate;
import com.matching.participate.repository.ParticipateRepository;
import com.matching.photo.domain.Photo;
import com.matching.photo.repository.PhotoRepository;
import com.matching.photo.service.PhotoService;
import com.matching.plan.domain.Plan;
import com.matching.plan.dto.PlanRequest;
import com.matching.plan.repository.PlanRepository;
import com.matching.post.domain.Category;
import com.matching.post.domain.ProjectPost;
import com.matching.post.domain.ProjectPostLike;
import com.matching.post.dto.PostSearchRequest;
import com.matching.post.dto.ProjectPostRequest;
import com.matching.post.dto.ProjectPostResponse;
import com.matching.post.dto.ProjectPostUpdateRequest;
import com.matching.post.repository.CategoryRepository;
import com.matching.post.repository.PostRepositoryQuerydsl;
import com.matching.post.repository.ProjectPostLikeRepository;
import com.matching.post.repository.ProjectPostRepository;
import com.matching.post.service.ProjectPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectPostServiceImpl implements ProjectPostService {
    private final ProjectPostRepository projectPostRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final ParticipateRepository participateRepository;
    private final CategoryRepository categoryRepository;
    private final PhotoRepository photoRepository;
    private final ProjectPostLikeRepository projectPostLikeRepository;
    private final PostRepositoryQuerydsl postRepositoryQuerydsl;

    private final PhotoService photoService;




    @Transactional
    @Override
    public Long writePost(ProjectPostRequest parameter, String id, List<MultipartFile> multipartFile) {
        Member member = memberRepository.findById(Long.parseLong(id))
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Category category = categoryRepository.findById(parameter.getCategoryId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        parameter.setMember(member);

        ProjectPost projectPost = projectPostRepository.save(ProjectPost.from(parameter, category));
        Plan plan = planRepository.save(Plan.from(parameter, member, projectPost));

        Participate participate = participateRepository.save(Participate.from(member, projectPost));

        if(multipartFile != null) {
            if(!multipartFile.isEmpty()) {
                photoService.savePhoto(projectPost, multipartFile);
            }
        }

        projectPost.setPlan(plan);
        participate.setStatus(Participate.ParticipateStatus.LEADER);

        return projectPost.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectPostResponse getPost(Long id) {
        ProjectPost projectPost = projectPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<Photo> photoList = photoRepository.findAllByProjectPost_Id(projectPost.getId())
                .orElse(new ArrayList<>());
        List<String> photoPathList = photoList.stream().map(Photo::getPathname)
                .collect(Collectors.toList());

        return ProjectPostResponse.from(projectPost, photoPathList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectPostResponse> getPostSearchList(PostSearchRequest parameter) {
        return ProjectPostResponse.fromEntitiesPage(
                postRepositoryQuerydsl.findAll(
                        PageRequest.of(parameter.getPageNum(), parameter.getPageSize()),
                        parameter.getKeyword()
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjectPostResponse> getPostByParticipant(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return ProjectPostResponse.fromEntitiesPage(
                projectPostRepository.findAllOrderByParticipateByPhotoCreatedAtDesc(
                        member.getId(),
                        PageRequest.of(0, 5)
                )
        );
    }
    @Transactional(readOnly = true)
    @Override
    public Page<ProjectPostResponse> getPostByWrite(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return ProjectPostResponse.fromEntitiesPage(
                projectPostRepository.findAllByAuthor_Id(
                        member.getId(),
                        PageRequest.of(0, 5, Sort.by("createdAt").descending())
                )
        );
    }

    @Transactional
    @Override
    public Long updatePost(Long postId, Long userId, ProjectPostUpdateRequest parameter) {
        ProjectPost projectPost = projectPostRepository.findByIdAndAuthor_Id(postId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        projectPost.update(parameter);

        return projectPost.getId();
    }

    @Transactional
    @Override
    public void deletePost(Long postId, Long userId) {
        ProjectPost projectPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Participate participate = participateRepository.findByParticipate_IdAndProjectPost_Id(member.getId(), projectPost.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPATE_NOT_FOUND));

        if(!participate.getStatus().equals(Participate.ParticipateStatus.LEADER)) {
            throw new CustomException(ErrorCode.PARTICIPATE_NOT_LEADER);
        } else {
            projectPostRepository.deleteById(projectPost.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProjectPostResponse> getPostByCategoryDesc(Long categoryId) {
        int pageNumber = 0; // 가져올 페이지 번호 (0부터 시작)
        int pageSize = 10; // 페이지당 결과 수
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<ProjectPost> postPage = projectPostRepository.findAllOrderByParticipantByPhotoCountByCategoryDesc(categoryId, pageable);

        return postPage.map(post -> {
            List<String> photoList = post.getPhotoList().stream()
                    .map(Photo::getPathname)
                    .collect(Collectors.toList());
            return ProjectPostResponse.from(post, photoList);
        });
    }

    @Transactional
    @Override
    public void toggleLike(Long memberId, Long projectPostId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(projectPostId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        projectPostLikeRepository.findByMember_IdAndProjectPost_Id(memberId, projectPostId)
                .ifPresentOrElse(
                        projectPostLikeRepository::delete,
                        () -> projectPostLikeRepository.save(ProjectPostLike.from(member, projectPost))
                );

    }

    @Transactional
    @Override
    public Long participate(PlanRequest parameter, String email, Long postId) {
        Member participant = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return null;
//        return planRepository.save(Plan.from(parameter, participant, projectPost)).getId();
    }

    @Transactional
    @Override
    public void completePlan(String email, Long planId) {
        Member participant = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

        if(plan.getParticipant().equals(participant)) {
            throw new IllegalArgumentException("해당 참가신청은 존재하지 않습니다.");
        }

        plan.setCompleted(true);
        planRepository.save(plan);
    }

}
