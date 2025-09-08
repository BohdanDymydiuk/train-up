package com.example.trainup.repository;

import com.example.trainup.model.Event;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT DISTINCT e FROM Event e "
            + "LEFT JOIN e.sport s "
            + "LEFT JOIN e.gym g "
            + "LEFT JOIN e.trainer t "
            + "LEFT JOIN e.location a "
            + "WHERE (:id IS NULL OR e.id = :id) "
            + "AND (:name IS NULL OR (CAST(e.name AS string) "
            + "LIKE CONCAT('%', CAST(:name AS string), '%'))) "
            + "AND (:sportId IS NULL OR s.id = :sportId) "
            + "AND ((CAST(:startOfDay AS timestamp) IS NULL "
            + "AND CAST(:endOfDay AS timestamp) IS NULL) "
            + "OR (e.dateTime >= :startOfDay AND e.dateTime < :endOfDay)) "
            + "AND (:gymId IS NULL OR g.id = :gymId) "
            + "AND (:trainerId IS NULL OR t.id = :trainerId) "
            + "AND (:onlineTraining IS NULL OR e.onlineTraining = :onlineTraining) "
            + "AND (:intensity IS NULL OR e.intensity = :intensity)"
            + "AND (:locationCountry IS NULL OR (a.country) "
            + "LIKE (CONCAT('%', CAST(:locationCountry AS string), '%'))) "
            + "AND (:locationCity IS NULL OR (a.city) "
            + "LIKE (CONCAT('%', CAST(:locationCity AS string), '%'))) "
            + "AND (:locationCityDistrict IS NULL OR (a.cityDistrict) "
            + "LIKE (CONCAT('%', CAST(:locationCityDistrict AS string), '%'))) "
            + "AND (:locationStreet IS NULL OR (a.street) "
            + "LIKE (CONCAT('%', CAST(:locationStreet AS string), '%'))) "
            + "AND (:locationHouse IS NULL OR (a.house) "
            + "LIKE (CONCAT('%', CAST(:locationHouse AS string), '%'))) "
    )
    Page<Event> findEventsByCriteria(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("sportId") Long sportId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay,
            @Param("gymId") Long gymId,
            @Param("trainerId") Long trainerId,
            @Param("onlineTraining") Boolean onlineTraining,
            @Param("intensity") Integer intensity,
            @Param("locationCountry") String locationCountry,
            @Param("locationCity") String locationCity,
            @Param("locationCityDistrict") String locationCityDistrict,
            @Param("locationStreet") String locationStreet,
            @Param("locationHouse") String locationHouse,
            Pageable pageable
    );

    @Query("SELECT e FROM Event e "
            + "LEFT JOIN FETCH e.trainer t "
            + "LEFT JOIN FETCH t.userCredentials uc "
            + "LEFT JOIN FETCH e.gym g "
            + "LEFT JOIN FETCH g.gymOwner go "
            + "LEFT JOIN FETCH go.userCredentials gou "
            + "WHERE e.id = :id")
    Optional<Event> findByIdWithDetails(@Param("id") Long id);
}
