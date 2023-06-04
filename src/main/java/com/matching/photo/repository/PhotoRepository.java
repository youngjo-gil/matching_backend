package com.matching.photo.repository;

import com.matching.photo.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<List<Photo>> findAllByProjectPost_Id(Long postId);
}
