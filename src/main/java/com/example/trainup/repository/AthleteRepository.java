package com.example.trainup.repository;

import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    Optional<Athlete> findByUserCredentials(UserCredentials userCredentials);
}
