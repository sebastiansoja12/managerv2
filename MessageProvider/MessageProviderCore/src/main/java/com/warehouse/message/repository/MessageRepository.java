package com.warehouse.message.repository;

import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;

public interface MessageRepository {
    Message findByTitle(final String title);
    List<Message> findBySender(final String sender);
    Message findByMessageId(final MessageId messageId);
    List<Message> findByLanguage(final String language);

    List<Message> findByShipmentStatus(final ShipmentStatus shipmentStatus);
}
