package com.example.trainup.controller;

import com.example.trainup.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class BaseUserController {
    private final PhotoService photoService;

    @PostMapping(value = "/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload user photo",
            description = "Allow Athlete, Trainer and GymOwner to upload photo."
    )
    public ResponseEntity<String> uploadPhoto(
            Authentication authentication,
            @RequestParam("file")
            @Parameter(
                    description = "Image file to upload (JPEG or PNG)",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary"))
            ) MultipartFile file
    ) {
        log.info("Attempting to upload photo for user: {}", authentication.getName());

        if (file.isEmpty()) {
            log.warn("Upload attempt with empty file for user: {}", authentication.getName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (!Arrays.asList("image/jpeg", "image/png").contains(file.getContentType())) {
            log.warn("Invalid file format uploaded by user: {}. Content type: {}",
                    authentication.getName(), file.getContentType());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid file format. Only JPEG and PNG are supported.");
        }

        try {
            String imageUrl = photoService.uploadUserPhoto(file, authentication);
            log.info("Photo uploaded successfully for user: {}. URL: {}",
                    authentication.getName(), imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (EntityNotFoundException e) {
            log.error("Entity not found during photo upload for user: {}",
                    authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            log.error("Security exception during photo upload for user: {}",
                    authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during photo upload for user: {}",
                    authentication.getName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
}
