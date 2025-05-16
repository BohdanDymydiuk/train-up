package com.example.trainup.repository;

import com.example.trainup.model.Gym;
import com.example.trainup.model.user.GymOwner;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GymRepository extends JpaRepository<Gym, Long> {
    @Query("SELECT DISTINCT g FROM Gym g "
            + "LEFT JOIN g.location a "
            + "LEFT JOIN g.sports s "
            + "LEFT JOIN g.trainers t "
            + "WHERE (:name IS NULL OR (g.name) LIKE (CONCAT('%', CAST(:name AS string), '%'))) "
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
            + "AND (:sportIds IS NULL OR s.id IN :sportIds) "
            + "AND (:trainerIds IS NULL OR t.id IN :trainerIds) "
            + "AND (:overallRating IS NULL OR g.overallRating >= :overallRating)")
    Page<Gym> findGymsByCriteria(
            @Param("name") String name,
            @Param("locationCountry") String locationCountry,
            @Param("locationCity") String locationCity,
            @Param("locationCityDistrict") String locationCityDistrict,
            @Param("locationStreet") String locationStreet,
            @Param("locationHouse") String locationHouse,
            @Param("sportIds") Set<Long> sportIds,
            @Param("trainerIds") Set<Long> trainerIds,
            @Param("overallRating") Float overallRating,
            Pageable pageable
    );

    Page<Gym> getGymsByGymOwner(GymOwner gymOwner, Pageable pageable);

    Gym getGymById(Long id);
}
