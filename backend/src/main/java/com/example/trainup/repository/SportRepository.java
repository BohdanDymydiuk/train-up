package com.example.trainup.repository;

import com.example.trainup.model.Sport;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SportRepository extends JpaRepository<Sport, Long> {
    @Query("SELECT DISTINCT s FROM Sport s "
            + "WHERE (:id IS NULL OR s.id = :id) "
            + "AND (:sportName IS NULL OR (CAST(s.sportName AS string)) "
            + "LIKE CONCAT('%', CAST(:sportName AS string), '%'))"
    )
    Page<Sport> findSportsByCriteria(
            @Param("id") Long id,
            @Param("sportName") String sportName,
            Pageable pageable
    );

    Optional<Sport> findBySportName(String sportName);
}
