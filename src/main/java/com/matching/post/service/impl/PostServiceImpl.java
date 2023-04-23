package com.matching.post.service.impl;

import com.matching.common.config.JwtTokenProvider;
import com.matching.member.domain.Member;
import com.matching.member.repository.MemberRepository;
import com.matching.post.domain.Post;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostResponse;
import com.matching.post.repository.PostRepository;
import com.matching.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PostRepository postRepository;

    private final MemberRepository memberRepository;


    @Transactional
    @Override
    public boolean writePost(PostRequest parameter, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getMemberEmailByToken(token);

        Member member = memberRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("회원이 없습니다."));

        parameter.setMember(member);

        Post post = postRepository.save(Post.from(parameter));

        return !ObjectUtils.isEmpty(post);
    }

    @Override
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        return PostResponse.from(post);
    }


    @Transactional
    @Override
    public Long participate(String email, Long postId) {

        return null;
    }


}
