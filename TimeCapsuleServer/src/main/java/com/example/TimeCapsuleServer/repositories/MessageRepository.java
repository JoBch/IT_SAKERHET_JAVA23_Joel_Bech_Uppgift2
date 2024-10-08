package com.example.TimeCapsuleServer.repositories;

import com.example.TimeCapsuleServer.entities.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    MessageEntity findByUserId(int id);

}
