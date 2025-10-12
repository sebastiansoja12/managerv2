package com.warehouse.qrcode.infrastructure.adapter.secondary.entity;

import com.warehouse.qrcode.domain.vo.ShipmentId;

import jakarta.persistence.*;
import jakarta.validation.Valid;

@Table(name = "shipment")
@Entity(name = "qrcode.ShipmentEntity")
public class ShipmentEntity {

    @Column(name = "shipment_id")
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "sender_telephone", nullable = false)
    private String senderTelephone;

    @Valid
    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Valid
    @Column(name = "sender_city", nullable = false)
    private String senderCity;

    @Valid
    @Column(name = "sender_street", nullable = false)
    private String senderStreet;

    @Valid
    @Column(name = "sender_postal_code", nullable = false)
    private String senderPostalCode;

    @Valid
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Valid
    @Column(name = "recipient_telephone", nullable = false)
    private String recipientTelephone;

    @Valid
    @Column(name = "recipient_first_name", nullable = false)
    private String recipientFirstName;

    @Valid
    @Column(name = "recipient_last_name", nullable = false)
    private String recipientLastName;

    @Valid
    @Column(name = "recipient_city", nullable = false)
    private String recipientCity;

    @Valid
    @Column(name = "recipient_street", nullable = false)
    private String recipientStreet;

    @Valid
    @Column(name = "recipient_postal_code", nullable = false)
    private String recipientPostalCode;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "shipment_related_id")
    @AttributeOverride(name = "value", column = @Column(name = "shipment_related_id"))
    private ShipmentId shipmentRelatedId;

    public ShipmentEntity() {
    }

    public ShipmentEntity(final String destination, final String firstName, final String lastName,
                          final ShipmentId shipmentRelatedId, final String recipientCity, final String recipientEmail,
                          final String recipientFirstName, final String recipientLastName, final String recipientPostalCode,
                          final String recipientStreet, final String recipientTelephone, final String senderCity,
                          final String senderEmail, final String senderPostalCode, final String senderStreet,
                          final String senderTelephone, final ShipmentId shipmentId) {
        this.destination = destination;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shipmentRelatedId = shipmentRelatedId;
        this.recipientCity = recipientCity;
        this.recipientEmail = recipientEmail;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.recipientPostalCode = recipientPostalCode;
        this.recipientStreet = recipientStreet;
        this.recipientTelephone = recipientTelephone;
        this.senderCity = senderCity;
        this.senderEmail = senderEmail;
        this.senderPostalCode = senderPostalCode;
        this.senderStreet = senderStreet;
        this.senderTelephone = senderTelephone;
        this.shipmentId = shipmentId;
    }

    public String getDestination() {
        return destination;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public ShipmentId getShipmentRelatedId() {
        return shipmentRelatedId;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public String getRecipientPostalCode() {
        return recipientPostalCode;
    }

    public String getRecipientStreet() {
        return recipientStreet;
    }

    public String getRecipientTelephone() {
        return recipientTelephone;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    public String getSenderStreet() {
        return senderStreet;
    }

    public String getSenderTelephone() {
        return senderTelephone;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }
}

