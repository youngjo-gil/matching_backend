package com.matching.photo.service;

import com.matching.post.domain.ProjectPost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {
    void savePhoto(ProjectPost projectPost, List<MultipartFile> multipartFile);
}
