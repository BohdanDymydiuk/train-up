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
            + "WHERE (:id IS NULL OR e.id = :id) "
            + "AND (:name IS NULL OR (CAST(e.name AS string) "
            + "LIKE CONCAT('%', CAST(:name AS string), '%'))) "
            + "AND (:sportId IS NULL OR s.id = :sportId) "
            + "AND (COALESCE(:dateTime, e.dateTime) = e.dateTime) "
            + "AND (:gymId IS NULL OR g.id = :gymId) "
            + "AND (:trainerId IS NULL OR t.id = :trainerId)")
    Page<Event> findEventsByCriteria(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("sportId") Long sportId,
            @Param("dateTime") LocalDateTime dateTime,
            @Param("gymId") Long gymId,
            @Param("trainerId") Long trainerId,
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
