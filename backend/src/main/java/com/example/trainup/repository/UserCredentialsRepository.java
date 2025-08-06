package com.example.trainup.repository;

import com.example.trainup.model.user.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
    Optional<UserCredentials> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM UserCredentials u WHERE u.email = :email "
            + "AND u.isDeleted = false")
    boolean existsByEmail(@Param("email") String email);
}
