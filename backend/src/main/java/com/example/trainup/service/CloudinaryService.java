package com.example.trainup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.trainup.exception.PhotoUploadException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Optional<Cloudinary> cloudinaryOptional;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!Arrays.asList("image/jpeg", "image/png").contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file format. "
                    + "Only JPEG and PNG are supported.");
        }

        Cloudinary cloudinary = cloudinaryOptional.orElseThrow(
                () -> new IllegalStateException("Cloudinary is not configured")
        );

        try {
            Map<String, Object> uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new PhotoUploadException("Failed to upload to Cloudinary: " + e.getMessage(), e);
        }
    }
}
