package com.warehouse.message.repository;

import com.warehouse.message.domain.model.Message;

import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {

    private final MessageReadRepository repository;

    public MessageRepositoryImpl(final MessageReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Message findByTitle(final String title) {
        return null;
    }

    @Override
    public List<Message> findBySender(final String sender) {
        return List.of();
    }

    @Override
    public Message findByMessageId(final String messageId) {
        return null;
    }
}
