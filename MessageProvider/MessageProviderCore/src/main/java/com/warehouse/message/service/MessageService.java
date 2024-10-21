package com.warehouse.message.service;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;

import java.util.List;

public interface MessageService {
    Message findByTitle(final String title);
    Message findByMessageId(final MessageId messageId);
    List<Message> findByLanguage(final String language);
    List<Message> findBySender(final String sender);
}
