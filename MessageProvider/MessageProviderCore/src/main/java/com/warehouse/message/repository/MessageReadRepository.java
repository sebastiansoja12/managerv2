package com.warehouse.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageEntity, Long> {

}
