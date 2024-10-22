package com.warehouse.message.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.domain.vo.SenderUpdateRequest;
import com.warehouse.message.domain.vo.TitleUpdateRequest;

public interface MessageService {
    Message findByTitle(final String title);
    Message findByMessageId(final MessageId messageId);
    List<Message> findByLanguage(final String language);
    List<Message> findBySender(final String sender);
    List<Message> findByShipmentStatus(final ShipmentStatus shipmentStatus);
    void updateMessageTitle(TitleUpdateRequest request);
    void updateMessageSender(SenderUpdateRequest request);
}
