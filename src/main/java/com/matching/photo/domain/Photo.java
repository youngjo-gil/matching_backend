package com.matching.photo.domain;

import com.matching.post.domain.ProjectPost;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pathname;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "post_id")
    private ProjectPost projectPost;

    public static Photo of(ProjectPost projectPost, String pathname) {
        return Photo.builder()
                .projectPost(projectPost)
                .pathname(pathname)
                .build();
    }
}
