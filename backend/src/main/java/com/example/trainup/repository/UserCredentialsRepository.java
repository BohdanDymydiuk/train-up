package com.example.trainup.repository;

import com.example.trainup.model.user.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmail(String email);

    boolean existsByEmail(String email);
}
