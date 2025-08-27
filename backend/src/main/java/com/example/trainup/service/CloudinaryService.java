package com.example.trainup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.trainup.exception.PhotoUploadException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
public class CloudinaryService {
    private final Optional<Cloudinary> cloudinaryOptional;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!Arrays.asList("image/jpeg", "image/png", "image/svg+xml")
                .contains(file.getContentType())) {
            throw new IllegalArgumentException("Invalid file format. "
                    + "Only JPEG, PNG, and SVG are supported.");
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

    public String uploadImage(InputStream inputStream, String fileName) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream is null");
        }

        String fileNameLower = fileName.toLowerCase();
        if (!fileNameLower.endsWith(".jpg") && !fileNameLower.endsWith(".jpeg")
                && !fileNameLower.endsWith(".png") && !fileNameLower.endsWith(".svg")) {
            throw new IllegalArgumentException("Invalid file format. "
                    + "Only JPEG, PNG, and SVG are supported.");
        }

        Cloudinary cloudinary = cloudinaryOptional.orElseThrow(
                () -> new IllegalStateException("Cloudinary is not configured")
        );

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] fileBytes = baos.toByteArray();
            log.info("Uploading file {} with size {} bytes", fileName, fileBytes.length);

            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    fileBytes,
                    Map.of(
                            "public_id", fileName.substring(0, fileName.lastIndexOf(".")),
                            "resource_type", "image"
                    )
            );
            return (String) uploadResult.get("url");
        } catch (IOException e) {
            throw new PhotoUploadException("Failed to upload to Cloudinary: " + e.getMessage(), e);
        }
    }
}
