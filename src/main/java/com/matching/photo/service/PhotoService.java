package com.matching.photo.service;

import com.matching.post.domain.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    void savePhoto(Post post, List<MultipartFile> multipartFile);
}
