package com.example.trainup.repository;

import com.example.trainup.model.enums.Gender;
import com.example.trainup.model.user.Trainer;
import com.example.trainup.model.user.UserCredentials;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserCredentials(UserCredentials userCredentials);

    @Query("SELECT DISTINCT t FROM Trainer t "
            + "LEFT JOIN t.location a "
            + "LEFT JOIN t.sports s "
            + "LEFT JOIN t.gyms g "
            + "WHERE t.isDeleted = false "
            + "AND (:firstName IS NULL OR (CAST(t.firstName AS string) "
            + "LIKE CONCAT('%', CAST(:firstName AS string), '%'))) "
            + "AND (:lastName IS NULL OR (CAST(t.lastName AS string) "
            + "LIKE CONCAT('%', CAST(:lastName AS string), '%'))) "
            + "AND (:gender IS NULL OR t.gender = :gender) "
            + "AND (:sportIds IS NULL OR s.id IN :sportIds) "
            + "AND (:gymIds IS NULL OR g.id IN :gymIds) "
            + "AND (:locationCountry IS NULL OR (a.country "
            + "LIKE CONCAT('%', CAST(:locationCountry AS string), '%'))) "
            + "AND (:locationCity IS NULL OR (a.city "
            + "LIKE CONCAT('%', CAST(:locationCity AS string), '%'))) "
            + "AND (:locationCityDistrict IS NULL OR (a.cityDistrict "
            + "LIKE CONCAT('%', CAST(:locationCityDistrict AS string), '%'))) "
            + "AND (:locationStreet IS NULL OR (a.street "
            + "LIKE CONCAT('%', CAST(:locationStreet AS string), '%'))) "
            + "AND (:locationHouse IS NULL OR (a.house "
            + "LIKE CONCAT('%', CAST(:locationHouse AS string), '%'))) "
            + "AND (:onlineTraining IS NULL OR t.onlineTraining = :onlineTraining) ")
    Page<Trainer> findTrainersByCriteria(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("gender") Gender gender,
            @Param("sportIds") Set<Long> sportIds,
            @Param("gymIds") Set<Long> gymIds,
            @Param("locationCountry") String locationCountry,
            @Param("locationCity") String locationCity,
            @Param("locationCityDistrict") String locationCityDistrict,
            @Param("locationStreet") String locationStreet,
            @Param("locationHouse") String locationHouse,
            @Param("onlineTraining") Boolean onlineTraining,
            Pageable pageable
    );

    List<Trainer> getTrainerById(Long id);

    List<Trainer> findTrainerById(Long id);

    @Query("SELECT t FROM Trainer t JOIN t.userCredentials uc WHERE uc.email = :email "
            + "AND t.isDeleted = false")
    Optional<Trainer> findByEmail(@Param("email") String email);
}
