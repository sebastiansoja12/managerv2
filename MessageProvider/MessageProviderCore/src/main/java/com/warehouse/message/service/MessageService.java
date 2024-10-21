package com.warehouse.message.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;

public interface MessageService {
    Message findByTitle(final String title);
    Message findByMessageId(final MessageId messageId);
    List<Message> findByLanguage(final String language);
    List<Message> findBySender(final String sender);
    List<Message> findByShipmentStatus(final ShipmentStatus shipmentStatus);
}
