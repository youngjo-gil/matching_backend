package com.matching.post.service.impl;

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
import com.matching.post.domain.Post;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostSearchRequest;
import com.matching.post.dto.PostUpdateRequest;
import com.matching.post.repository.CategoryRepository;
import com.matching.post.repository.PostRepository;
import com.matching.post.repository.PostRepositoryQuerydsl;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final ParticipateRepository participateRepository;
    private final CategoryRepository categoryRepository;
    private final PhotoRepository photoRepository;
    private final PostRepositoryQuerydsl postRepositoryQuerydsl;

    private final PhotoService photoService;




    @Transactional
    @Override
    public Long writePost(PostRequest parameter, String id, List<MultipartFile> multipartFile) {
        Member member = memberRepository.findById(Long.parseLong(id))
                        .orElseThrow(() -> new RuntimeException("회원이 없습니다."));
        Category category = categoryRepository.findById(parameter.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("해당 카테고리가 없습니다."));


        parameter.setMember(member);

        Post post = postRepository.save(Post.from(parameter, category));
        Plan plan = planRepository.save(Plan.from(parameter, member, post));

        Participate participate = participateRepository.save(Participate.from(member, post));

        if(multipartFile != null) {
            if(!multipartFile.isEmpty()) {
                photoService.savePhoto(post, multipartFile);
            }
        }

        post.setPlan(plan);
        participate.setStatus(Participate.ParticipateStatus.LEADER);

        return post.getId();
    }

    @Override
    @Transactional
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        List<Photo> photoList = photoRepository.findAllByPost_Id(post.getId())
                .orElse(null);
        List<String> test = photoList.stream().map(item -> item.getPathname())
                .collect(Collectors.toList());

        return PostResponse.from(post, test);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getPostSearchList(PostSearchRequest parameter) {
        return PostResponse.fromEntitiesPage(
                postRepositoryQuerydsl.findAll(
                        PageRequest.of(parameter.getPageNum(), parameter.getPageSize()),
                        parameter.getKeyword()
                )
        );
    }

    // 참가중인 Post 조회ㅔ
    @Override
    public Page<PostResponse> getPostByParticipant(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        return PostResponse.fromEntitiesPage(
                postRepository.findAllOrderByParticipateByPhotoCreatedAtDesc(
                        member.getId(),
                        PageRequest.of(0, 5)
                )
        );
    }

    // 작성한 글 조회
    @Override
    public Page<PostResponse> getPostByWrite(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        return PostResponse.fromEntitiesPage(
                postRepository.findAllByAuthor_Id(
                        member.getId(),
                        PageRequest.of(0, 5, Sort.by("createdAt").descending())
                )
        );
    }

    @Transactional
    @Override
    public Long updatePost(Long postId, Long userId, PostUpdateRequest parameter) {
        Post post = postRepository.findByIdAndAuthor_Id(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 존재하지 않습니다."));

        post.update(parameter);

        return post.getId();
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 존재하지 않습니다."));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));

        Participate participate = participateRepository.findByParticipate_IdAndPost_Id(member.getId(), post.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트 참가자가 아닙니다."));

        if(!participate.getStatus().equals(Participate.ParticipateStatus.LEADER)) {
            throw new IllegalArgumentException("해당 포스트 리더가 아닙니다.");
        } else {
            postRepository.deleteById(post.getId());
        }
    }

    @Override
    public Page<PostResponse> getPostByCategoryDesc(Long categoryId) {
        int pageNumber = 0; // 가져올 페이지 번호 (0부터 시작)
        int pageSize = 10; // 페이지당 결과 수
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<PostResponse> responses = new ArrayList<>();

        Page<Post> postPage = postRepository.findAllOrderByParticipantByPhotoCountByCategoryDesc(categoryId, pageable);

        return postPage.map(post -> {
            List<String> photoList = post.getPhotoList().stream()
                    .map(Photo::getPathname)
                    .collect(Collectors.toList());
            return PostResponse.from(post, photoList);
        });
    }

    @Transactional
    @Override
    public Long participate(PlanRequest parameter, String email, Long postId) {
        Member participant = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));


//        return planRepository.save(Plan.from(parameter, participant, post)).getId();

        return null;
    }

    @Transactional
    @Override
    public void completePlan(String email, Long planId) {
        Member participant = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플랜이 존재하지 않습니다."));

        if(plan.getParticipant().equals(participant)) {
            throw new IllegalArgumentException("해당 참가신청은 존재하지 않습니다.");
        }

        plan.setCompleted(true);
        planRepository.save(plan);
    }

}
