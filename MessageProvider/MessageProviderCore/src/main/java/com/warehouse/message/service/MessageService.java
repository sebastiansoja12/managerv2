package com.warehouse.message.service;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;

import java.util.List;

public interface MessageService {
    Message findByTitle(final String title);
    List<Message> findBySender(final String sender);
    Message findByMessageId(final MessageId messageId);
}
