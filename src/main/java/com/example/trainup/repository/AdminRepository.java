package com.example.trainup.repository;

import com.example.trainup.model.user.Admin;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.roles WHERE a.email = :email")
    Optional<Admin> findByEmail(@Param("email") String email);
}
