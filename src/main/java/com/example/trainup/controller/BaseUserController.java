package com.example.trainup.controller;

import com.example.trainup.repository.BaseUserRepository;
import com.example.trainup.service.UserPhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
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
    private final UserPhotoService userPhotoService;
    private final BaseUserRepository baseUserRepository;

    @PostMapping("/photo")
    public ResponseEntity<String> uploadPhoto(
            Authentication authentication,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = userPhotoService.uploadPhotoWithAuth(authentication, file);
            return ResponseEntity.ok(imageUrl);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
