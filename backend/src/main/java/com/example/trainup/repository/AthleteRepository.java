package com.example.trainup.repository;

import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Athlete;
import com.example.trainup.model.user.UserCredentials;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    Optional<Athlete> findByUserCredentials(UserCredentials userCredentials);

    @Query("SELECT DISTINCT a FROM Athlete a "
            + "LEFT JOIN a.sports s "
            + "WHERE a.isDeleted = false "
            + "AND (:firstName IS NULL OR (CAST(a.firstName AS string) "
            + "LIKE CONCAT('%', CAST(:firstName AS string), '%'))) "
            + "AND (:lastName IS NULL OR (CAST(a.lastName AS string) "
            + "LIKE CONCAT('%', CAST(:lastName AS string), '%'))) "
            + "AND (:gender IS NULL OR a.gender = :gender) "
            + "AND (:dateOfBirth IS NULL OR a.dateOfBirth = :dateOfBirth) "
            + "AND (:sportIds IS NULL OR s.id IN :sportIds) "
            + "AND (:emailPermission IS NULL OR a.emailPermission = :emailPermission) "
            + "AND (:phonePermission IS NULL OR a.phonePermission = :phonePermission)")
    Page<Athlete> findAthleteByCriteria(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("gender") Gender gender,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("sportIds") Set<Long> sportIds,
            @Param("emailPermission") Boolean emailPermission,
            @Param("phonePermission") Boolean phonePermission,
            Pageable pageable
    );

    @Query("SELECT a FROM Athlete a JOIN a.userCredentials uc WHERE uc.email = :email "
            + "AND a.isDeleted = false")
    Optional<Athlete> findByEmail(@Param("email") String email);
}
