package com.example.trainup.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Log4j2
public class UserPhotoService {
    private final AthleteRepository athleteRepository;
    private final TrainerRepository trainerRepository;
    private final GymOwnerRepository gymOwnerRepository;
    private final UserCredentialsRepository userCredentialsRepository;

    @Autowired(required = false)
    private Cloudinary cloudinary;

    @Autowired
    public UserPhotoService(AthleteRepository athleteRepository,
                            TrainerRepository trainerRepository,
                            GymOwnerRepository gymOwnerRepository,
                            UserCredentialsRepository userCredentialsRepository) {
        this.athleteRepository = athleteRepository;
        this.trainerRepository = trainerRepository;
        this.gymOwnerRepository = gymOwnerRepository;
        this.userCredentialsRepository = userCredentialsRepository;
    }

    public String uploadPhoto(MultipartFile file, Long userId) throws IOException {
        if (cloudinary == null) {
            throw new IllegalStateException("Cloudinary is not configured for upload");
        }

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");

        BaseUser user = findUserById(userId);
        user.setProfileImageUrl(imageUrl);
        saveUser(user);

        return imageUrl;
    }

    private BaseUser findUserById(Long userId) {
        Optional<Athlete> athlete = athleteRepository.findById(userId);
        if (athlete.isPresent()) {
            return athlete.get();
        }

        Optional<Trainer> trainer = trainerRepository.findById(userId);
        if (trainer.isPresent()) {
            return trainer.get();
        }

        Optional<GymOwner> gymOwner = gymOwnerRepository.findById(userId);
        if (gymOwner.isPresent()) {
            return gymOwner.get();
        }
        throw new EntityNotFoundException("User not found with id: " + userId);
    }

    private void saveUser(BaseUser user) {
        if (user instanceof Athlete) {
            athleteRepository.save((Athlete) user);
        } else if (user instanceof Trainer) {
            trainerRepository.save((Trainer) user);
        } else if (user instanceof GymOwner) {
            gymOwnerRepository.save((GymOwner) user);
        } else {
            throw new IllegalArgumentException("Unknown user type: " + user.getClass().getName());
        }
    }

    public String uploadPhotoWithAuth(
            Authentication authentication,
            MultipartFile file
    ) throws IOException {
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

        try {
            Map<String, Object> uploadResult = cloudinary
                    .uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            user.setProfileImageUrl(imageUrl);
            saveUser(user);

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary: " + e.getMessage(), e);
        }
    }

    private UserCredentials extractUserCredentials(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User is not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userCredentialsRepository.findByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "User credentials not found for email: " + email));
        }
        throw new IllegalStateException("Unknown principal type: "
                + principal.getClass().getName());
    }

    private BaseUser findUserByCredentials(UserCredentials userCredentials) {
        Optional<Athlete> athlete = athleteRepository.findByUserCredentials(userCredentials);
        if (athlete.isPresent()) {
            return athlete.get();
        }

        Optional<Trainer> trainer = trainerRepository.findByUserCredentials(userCredentials);
        if (trainer.isPresent()) {
            return trainer.get();
        }

        Optional<GymOwner> gymOwner = gymOwnerRepository.findByUserCredentials(userCredentials);
        if (gymOwner.isPresent()) {
            return gymOwner.get();
        }

        return null;
    }
}
