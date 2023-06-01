package com.matching.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matching.common.domain.BaseEntity;
import com.matching.member.domain.Member;
import com.matching.participate.domain.Participate;
import com.matching.photo.domain.Photo;
import com.matching.plan.domain.Plan;
import com.matching.post.dto.PostRequest;
import com.matching.post.dto.PostUpdateRequest;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
@Entity
@Table(name = "post")
//@NamedEntityGraph(
//        name = "Post.photoParticipate",
//        attributeNodes = {
//                @NamedAttributeNode("author"),
//                @NamedAttributeNode("plan"),
//                @NamedAttributeNode("participateList"),
//                @NamedAttributeNode(value = "photoList",subgraph = "photoList")
//        },
//        subgraphs = @NamedSubgraph(name ="photoList",attributeNodes = {@NamedAttributeNode("photo")})
//)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;

    //(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    @JsonIgnore
    private Plan plan;

    // 글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member author;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Participate> participateList = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Photo> photoList = new ArrayList<>();

    public void update(PostUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public static Post from(PostRequest parameter, Category category) {
        return Post.builder()
                .title(parameter.getTitle())
                .content(parameter.getContent())
                .category(category)
                .author(parameter.getMember())
                .build();
    }
}
