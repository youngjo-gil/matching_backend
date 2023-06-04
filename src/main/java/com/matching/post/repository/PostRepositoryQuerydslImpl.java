package com.matching.post.repository;

import com.matching.post.domain.ProjectPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.matching.post.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryQuerydslImpl implements PostRepositoryQuerydsl {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ProjectPost> findAll(Pageable pageable, String keyword) {
        return new PageImpl<>(
                searchPost(keyword, pageable)
        );
    }

    private List<ProjectPost> searchPost(String keyword, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        eqTitle(keyword)
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression eqTitle(String keyword) {
        return !StringUtils.hasText(keyword) ? null : post.title.contains(keyword);
    }

}
