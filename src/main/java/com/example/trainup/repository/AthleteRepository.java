package com.example.trainup.repository;

import com.example.trainup.model.user.Athlete;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    @Query("SELECT a FROM Athlete a LEFT JOIN FETCH a.roles WHERE a.email = :email")
    Optional<Athlete> findByEmail(@Param("email") String email);
}
