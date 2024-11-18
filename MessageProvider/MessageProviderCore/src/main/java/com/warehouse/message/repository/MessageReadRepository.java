package com.warehouse.message.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageEntity, Long> {

    Optional<MessageEntity> findByTitle(final String title);

    List<MessageEntity> findBySender(final String sender);

    List<MessageEntity> findByLanguage(final String language);

    List<MessageEntity> findByShipmentStatus(final ShipmentStatus shipmentStatus);
}
