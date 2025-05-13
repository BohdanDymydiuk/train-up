package com.example.trainup.repository;

import com.example.trainup.model.Gym;
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
            + "WHERE (:name IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:locationCountry IS NULL OR LOWER(a.country) "
            + "LIKE LOWER(CONCAT('%', :locationCountry, '%'))) "
            + "AND (:locationCity IS NULL OR LOWER(a.city) "
            + "LIKE LOWER(CONCAT('%', :locationCity, '%'))) "
            + "AND (:locationCityDistrict IS NULL OR LOWER(a.cityDistrict) "
            + "LIKE LOWER(CONCAT('%', :locationCityDistrict, '%'))) "
            + "AND (:locationStreet IS NULL OR LOWER(a.street) "
            + "LIKE LOWER(CONCAT('%', :locationStreet, '%'))) "
            + "AND (:locationHouse IS NULL OR LOWER(a.house) "
            + "LIKE LOWER(CONCAT('%', :locationHouse, '%'))) "
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
}
