package com.example.trainup.service;

import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import com.example.trainup.repository.AthleteRepository;
import com.example.trainup.repository.GymOwnerRepository;
import com.example.trainup.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final GymOwnerRepository gymOwnerRepository;
    private final AthleteRepository athleteRepository;
    private final TrainerRepository trainerRepository;

    public <T> T getCurrentUserByType(Class<T> userType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserCredentials userCredentials)) {
            throw new IllegalStateException("User is not authenticated "
                    + "or principal is not UserCredentials");
        }
        UserCredentials.UserType credentialsType = userCredentials.getUserType();
        String email = userCredentials.getEmail();

        switch (credentialsType) {
            case GYM_OWNER:
                if (GymOwner.class.isAssignableFrom(userType)) {
                    return userType.cast(gymOwnerRepository.findByUserCredentials(userCredentials)
                            .orElseThrow(() -> new IllegalStateException(
                                    "GymOwner not found for user: " + email)));
                }
                break;

            case ATHLETE:
                if (Athlete.class.isAssignableFrom(userType)) {
                    return userType.cast(athleteRepository.findByUserCredentials(userCredentials)
                            .orElseThrow(() -> new IllegalStateException(
                                    "Athlete not found for user: " + email)));
                }
                break;
            case TRAINER:
                if (Trainer.class.isAssignableFrom(userType)) {
                    return userType.cast(trainerRepository.findByUserCredentials(userCredentials)
                            .orElseThrow(() -> new IllegalStateException(
                                    "Trainer not found for user: " + email)));
                }
                break;
            default:
                throw new IllegalStateException("Unsupported user type: " + credentialsType);
        }
        throw new IllegalArgumentException("Requested user type " + userType.getSimpleName()
                + " does not match credentials type " + credentialsType);
    }

    public UserCredentials getCurrentUserCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !(authentication.getPrincipal() instanceof UserCredentials userCredentials)) {
            throw new IllegalStateException(
                    "User is not authenticated or principal is not UserCredentials");
        }
        return userCredentials;
    }
}
