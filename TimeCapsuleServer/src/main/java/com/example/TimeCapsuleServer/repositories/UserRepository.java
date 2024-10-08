package com.example.TimeCapsuleServer.repositories;

import com.example.TimeCapsuleServer.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
