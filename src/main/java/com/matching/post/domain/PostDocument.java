package com.matching.post.domain;

import com.matching.member.domain.Member;
import com.matching.plan.domain.Plan;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "post")
//@Mapping(mappingPath = "elastic/post-mapping.json")
@Setting(settingPath = "elastic/post-setting.json")
public class PostDocument {
    @Id
    private Long id;
    private String title;
    private String content;
    private Category category;
    private Plan plan;
    private Member author;

    public static PostDocument from(Post post) {
        return PostDocument.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .plan(post.getPlan())
                .author(post.getAuthor())
                .build();
    }
}
