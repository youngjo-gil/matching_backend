package com.matching.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class FileUtil {
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public String convert(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        return storeFileName;
    }

    private String createStoreFileName(String originalFilename) {
        String fileExtension = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();

        return uuid + "." + fileExtension;
    }

    private String extractExt(String originalFilename) {
        int fileExtensionIndex = originalFilename.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        return originalFilename.substring(fileExtensionIndex + 1);
    }
}
