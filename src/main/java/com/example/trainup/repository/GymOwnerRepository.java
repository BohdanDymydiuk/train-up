package com.example.trainup.repository;

import com.example.trainup.model.user.GymOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymOwnerRepository extends JpaRepository<GymOwner, Long> {
}
