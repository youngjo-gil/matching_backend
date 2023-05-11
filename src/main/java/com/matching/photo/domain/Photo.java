package com.matching.photo.domain;

import com.matching.post.domain.Post;
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
    private Post post;

    public static Photo of(Post post, String pathname) {
        return Photo.builder()
                .post(post)
                .pathname(pathname)
                .build();
    }
}
