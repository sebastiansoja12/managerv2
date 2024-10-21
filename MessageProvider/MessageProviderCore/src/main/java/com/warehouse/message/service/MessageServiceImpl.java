package com.warehouse.message.service;

import java.util.List;

import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.repository.MessageRepository;

public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message findByTitle(final String title) {
        return messageRepository.findByTitle(title);
    }

    @Override
    public List<Message> findBySender(final String sender) {
        return messageRepository.findBySender(sender);
    }

    @Override
    public Message findByMessageId(final MessageId messageId) {
        return messageRepository.findByMessageId(messageId);
    }
}
