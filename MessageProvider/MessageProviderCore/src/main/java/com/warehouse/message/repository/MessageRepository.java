package com.warehouse.message.repository;

import com.warehouse.message.domain.model.Message;

import java.util.List;

public interface MessageRepository {
    Message findByTitle(final String title);
    List<Message> findBySender(final String sender);
    Message findByMessageId(final String messageId);
}
