package com.warehouse.message.domain.model;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.MessageId;
import com.warehouse.message.repository.MessageEntity;

public class Message {
    
    private MessageId messageId;
    
    private String title;
    
    private ShipmentStatus shipmentStatus;
    
    private String language;
    
    private String messageContent;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private String sender;

    public Message() {

    }

	public Message(final MessageId messageId, final String title, final ShipmentStatus shipmentStatus,
			final String language, final String messageContent, final LocalDateTime createdAt,
			final LocalDateTime updatedAt, final String sender) {
		this.messageId = messageId;
		this.title = title;
		this.shipmentStatus = shipmentStatus;
		this.language = language;
		this.messageContent = messageContent;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.sender = sender;
	}

    public static Message empty() {
        return new Message();
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public String getTitle() {
        return title;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public String getLanguage() {
        return language;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getSender() {
        return sender;
    }

    private void markAsModified() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateTitle(final String title) {
        this.title = title;
        markAsModified();
    }
    
    public void updateShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
        markAsModified();
    }
    
    public void updateLanguage(final String language) {
        this.language = language;
        markAsModified();
    }
    
    public void updateMessageContent(final String messageContent) {
        this.messageContent = messageContent;
        markAsModified();
    }

    public void updateSender(final String sender) {
        this.sender = sender;
        markAsModified();
    }
    
	public static Message from(final MessageEntity entity) {
		final MessageId id = new MessageId(entity.getId());
		return new Message(id, entity.getTitle(), entity.getShipmentStatus(), entity.getLanguage(),
				entity.getMessageContent(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getSender());
	}
}
