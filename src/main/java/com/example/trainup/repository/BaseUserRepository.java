package com.example.trainup.repository;

import com.example.trainup.model.user.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
}
