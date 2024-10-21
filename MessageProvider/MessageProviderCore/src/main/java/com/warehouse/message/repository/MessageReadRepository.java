package com.warehouse.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageEntity, Long> {

    Optional<MessageEntity> findByTitle(final String title);

    List<MessageEntity> findBySender(String sender);
}
