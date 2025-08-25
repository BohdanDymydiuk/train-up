package com.example.trainup.service;

import com.example.trainup.model.Event;
import com.example.trainup.model.EventPhoto;
import com.example.trainup.model.Gym;
import com.example.trainup.model.GymPhoto;
import com.example.trainup.model.Sport;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.BaseUser;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.EventRepository;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.repository.GymRepository;
import com.example.trainup.repository.SportRepository;
import com.example.trainup.repository.TrainerRepository;
import com.example.trainup.repository.UserCredentialsRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class PhotoService {
    private final AthleteRepository athleteRepository;
    private final TrainerRepository trainerRepository;
    private final GymOwnerRepository gymOwnerRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final SportRepository sportRepository;
    private final GymRepository gymRepository;
    private final EventRepository eventRepository;
    private final CloudinaryService cloudinaryService;

    public String uploadUserPhoto(MultipartFile file, Authentication authentication) {
        UserCredentials credentials = extractUserCredentials(authentication);
        BaseUser user = findUserByCredentials(credentials);
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }

        String imageUrl = cloudinaryService.uploadImage(file);
        user.setProfileImageUrl(imageUrl);
        saveUser(user);
        log.info("User photo uploaded: {}", imageUrl);
        return imageUrl;
    }

    public String uploadSportIcon(Long sportId, MultipartFile file) {
        Sport sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new EntityNotFoundException("Sport not found with id: "
                        + sportId));

        String imageUrl = cloudinaryService.uploadImage(file);
        sport.setSportIconUrl(imageUrl);
        sportRepository.save(sport);
        log.info("Sport icon uploaded: {}", imageUrl);
        return imageUrl;
    }

    public String uploadGymPhoto(Long gymId, MultipartFile file) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Gym not found with id: " + gymId));
        if (gym.getPhotos().size() >= 5) {
            throw new IllegalStateException("Maximum number of gym photos (5) reached");
        }

        String imageUrl = cloudinaryService.uploadImage(file);
        GymPhoto photo = new GymPhoto();
        photo.setImageUrl(imageUrl);
        photo.setGym(gym);
        gym.getPhotos().add(photo);
        gymRepository.save(gym);
        log.info("Gym photo uploaded: {}", imageUrl);
        return imageUrl;
    }

    public String uploadEventPhoto(Long eventId, MultipartFile file) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Event not found with id: " + eventId));
        if (event.getPhotos().size() >= 3) {
            throw new IllegalStateException("Maximum number of event photo (3) reached");
        }

        String imageUrl = cloudinaryService.uploadImage(file);
        EventPhoto photo = new EventPhoto();
        photo.setImageUrl(imageUrl);
        photo.setEvent(event);
        event.getPhotos().add(photo);
        eventRepository.save(event);
        log.info("Event photo uploaded: {}", imageUrl);
        return imageUrl;
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
}
