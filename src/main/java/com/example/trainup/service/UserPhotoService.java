package com.example.trainup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.trainup.exception.PhotoUploadException;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.BaseUser;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserPhotoService {
    private final AthleteRepository athleteRepository;
    private final TrainerRepository trainerRepository;
    private final GymOwnerRepository gymOwnerRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final Optional<Cloudinary> cloudinary;

    public String uploadPhoto(MultipartFile file, Long userId) {
        Cloudinary actualCloudinary = cloudinary.orElseThrow(
                () -> new IllegalStateException("Cloudinary service is not configured or available "
                        + "for upload."));

        try {
            Map<String, Object> uploadResult = actualCloudinary
                    .uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            BaseUser user = findUserById(userId);
            user.setProfileImageUrl(imageUrl);
            saveUser(user);

            return imageUrl;
        } catch (IOException e) {
            throw new PhotoUploadException("Failed to upload photo to Cloudinary: "
                    + e.getMessage(), e);
        }
    }

    private BaseUser findUserById(Long userId) {
        return athleteRepository.findById(userId)
                .map(BaseUser.class::cast)
                .or(() -> trainerRepository.findById(userId).map(BaseUser.class::cast))
                .or(() -> gymOwnerRepository.findById(userId).map(BaseUser.class::cast))
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + userId));
    }

    private void saveUser(BaseUser user) {
        if (user instanceof Athlete athlete) {
            athleteRepository.save(athlete);
        } else if (user instanceof Trainer trainer) {
            trainerRepository.save(trainer);
        } else if (user instanceof GymOwner gymOwner) {
            gymOwnerRepository.save(gymOwner);
        } else {
            throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
        }
    }

    public String uploadPhotoWithAuth(
            Authentication authentication,
            MultipartFile file
    ) {
        UserCredentials userCredentials = extractUserCredentials(authentication);
        BaseUser user = findUserByCredentials(userCredentials);
        if (user == null) {
            throw new EntityNotFoundException("User not found with credentials: "
                    + userCredentials.getEmail());
        }

        log.debug("Received file: name={}, size={}, isEmpty={}",
                file.getOriginalFilename(), file.getSize(), file.isEmpty());

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty or missing");
        }

        Cloudinary actualCloudinary = cloudinary.orElseThrow(
                () -> new IllegalStateException("Cloudinary service is not configured or available "
                        + "for upload."));

        try {
            Map<String, Object> uploadResult = actualCloudinary
                    .uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            user.setProfileImageUrl(imageUrl);
            saveUser(user);

            return imageUrl;
        } catch (IOException e) {
            throw new PhotoUploadException("Failed to upload file to Cloudinary: "
                    + e.getMessage(), e);
        }
    }

    private UserCredentials extractUserCredentials(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userCredentialsRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User credentials not found for email: " + email));
        }
        throw new IllegalStateException("Unknown principal type: "
                + principal.getClass().getName());
    }

    private BaseUser findUserByCredentials(UserCredentials userCredentials) {
        return athleteRepository.findByUserCredentials(userCredentials)
                .map(BaseUser.class::cast)
                .or(() -> trainerRepository.findByUserCredentials(userCredentials)
                        .map(BaseUser.class::cast))
                .or(() -> gymOwnerRepository.findByUserCredentials(userCredentials)
                        .map(BaseUser.class::cast))
                .orElse(null);
    }
}
