package com.warehouse.message.domain.service;

import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.domain.model.Message;
import com.warehouse.message.domain.vo.MessageContentUpdateRequest;
import com.warehouse.message.domain.vo.SenderUpdateRequest;
import com.warehouse.message.domain.vo.TitleUpdateRequest;
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
    public List<Message> findByShipmentStatus(final ShipmentStatus shipmentStatus) {
        return messageRepository.findByShipmentStatus(shipmentStatus);
    }

    @Override
    public void updateMessageTitle(final TitleUpdateRequest request) {
        final Message message = this.messageRepository.findByMessageId(request.getMessageId());
        message.updateTitle(request.getTitle());
        this.messageRepository.update(message);
    }

    @Override
    public void updateMessageSender(final SenderUpdateRequest request) {
        final Message message = this.messageRepository.findByMessageId(request.messageId());
        message.updateSender(request.sender());
        this.messageRepository.update(message);
    }

    @Override
    public void updateMessageContent(final MessageContentUpdateRequest request) {
        final Message message = this.messageRepository.findByMessageId(request.messageId());
        message.updateMessageContent(request.messageContent());
        this.messageRepository.update(message);
    }

    @Override
    public Message findByMessageId(final MessageId messageId) {
        return messageRepository.findByMessageId(messageId);
    }

    @Override
    public List<Message> findByLanguage(final String language) {
        return messageRepository.findByLanguage(language);
    }
}
