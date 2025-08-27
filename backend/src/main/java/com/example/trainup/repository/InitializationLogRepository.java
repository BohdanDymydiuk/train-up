package com.example.trainup.repository;

import com.example.trainup.model.InitializationLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitializationLogRepository extends JpaRepository<InitializationLog, Long> {
    Optional<InitializationLog> findByInitializationType(String initializationType);
}
