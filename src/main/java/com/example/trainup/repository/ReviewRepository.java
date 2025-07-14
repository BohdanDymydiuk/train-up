package com.example.trainup.repository;

import com.example.trainup.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT DISTINCT r FROM Review r "
            + "LEFT JOIN r.author a "
            + "LEFT JOIN r.gym g "
            + "LEFT JOIN r.trainer t "
            + "WHERE (:id IS NULL OR r.id = :id) "
            + "AND (:rating IS NULL OR r.rating = :rating) "
            + "AND (:authorId IS NULL OR a.id = :authorId) "
            + "AND (:gymId IS NULL OR g.id = :gymId) "
            + "AND (:trainerId IS NULL OR t.id = :trainerId)")
    Page<Review> findReviewsByCriteria(
            @Param("id") Long id,
            @Param("rating") Integer rating,
            @Param("authorId") Long authorId,
            @Param("gymId") Long gymId,
            @Param("trainerId") Long trainerId,
            Pageable pageable
    );
}
