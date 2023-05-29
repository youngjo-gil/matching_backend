package com.matching.post.service.impl;

import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.participate.domain.Participate;
import com.matching.participate.repository.ParticipateRepository;
import com.matching.photo.service.PhotoService;
import com.matching.plan.domain.Plan;
import com.matching.plan.dto.PlanRequest;
import com.matching.plan.repository.PlanRepository;
import com.matching.post.domain.Category;
import com.matching.post.domain.Post;
import com.matching.post.domain.PostDocument;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.dto.PostUpdateRequest;
import com.matching.post.repository.CategoryRepository;
import com.matching.post.repository.PostRepository;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;
    private final ParticipateRepository participateRepository;
    private final CategoryRepository categoryRepository;

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
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        return PostResponse.from(post);
    }

    @Transactional
    @Override
    public Long updatePost(Long postId, Long userId, PostUpdateRequest parameter) {
        Post post = postRepository.findByIdAndAuthor_Id(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 존재하지 않습니다."));

        post.update(parameter);

        return post.getId();
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
