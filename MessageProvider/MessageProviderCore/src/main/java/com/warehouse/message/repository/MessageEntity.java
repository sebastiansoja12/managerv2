package com.warehouse.message.repository;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ShipmentStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Column(nullable = false)
    private String language;

    @Lob
    @Column(nullable = false)
    private String messageContent;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String sender;


    public MessageEntity() {
        this.createdAt = LocalDateTime.now();
    }

	public MessageEntity(final String title, final ShipmentStatus shipmentStatus, final String language,
			final String messageContent, final String sender) {
        this.title = title;
        this.shipmentStatus = shipmentStatus;
        this.language = language;
        this.messageContent = messageContent;
        this.sender = sender;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(final ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(final String messageContent) {
        this.messageContent = messageContent;
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

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}


