package com.matching.photo.service.impl;

import com.matching.aws.service.AwsS3Service;
import com.matching.photo.domain.Photo;
import com.matching.photo.repository.PhotoRepository;
import com.matching.photo.service.PhotoService;
import com.matching.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final AwsS3Service awsS3Service;
    private final PhotoRepository photoRepository;
    @Override
    public void savePhoto(Post post, List<MultipartFile> multipartFile) {
        List<String> imgPaths = awsS3Service.upload(multipartFile);

        for (String img : imgPaths) {
            Photo photo = Photo.of(post, img);
            photoRepository.save(photo);
        }
    }
}
