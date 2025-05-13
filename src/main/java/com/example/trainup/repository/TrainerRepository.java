package com.example.trainup.repository;

import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserCredentials(UserCredentials userCredentials);
}
