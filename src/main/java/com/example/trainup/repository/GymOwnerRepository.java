package com.example.trainup.repository;

import com.example.trainup.model.user.GymOwner;
import com.example.trainup.model.user.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymOwnerRepository extends JpaRepository<GymOwner, Long> {
    Optional<GymOwner> findByUserCredentials(UserCredentials userCredentials);
}
