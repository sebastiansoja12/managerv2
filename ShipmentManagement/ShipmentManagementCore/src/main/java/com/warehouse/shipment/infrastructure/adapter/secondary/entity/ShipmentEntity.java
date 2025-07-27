package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Shipment;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment")
@Entity(name = "shipment.ShipmentEntity")
@EntityListeners(AuditingEntityListener.class)
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
    @Pattern(regexp="\\d{2}-\\d{3}")
    @Column(name = "recipient_postal_code", nullable = false)
    private String recipientPostalCode;

    @Column(name = "parcel_size", nullable = false)
    private ShipmentSize shipmentSize;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    private ShipmentStatus shipmentStatus;

    @Column(name = "type", nullable = false)
    private ShipmentType shipmentType;

    @Column(name = "shipment_related_id")
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_related_id"))
    private ShipmentId shipmentRelatedId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @Column(name = "origin_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country originCountry;

    @Column(name = "destination_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country destinationCountry;

    @Column(name = "shipment_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentPriority shipmentPriority;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id")
    private DangerousGoodEntity dangerousGood;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

	public ShipmentEntity(final ShipmentId shipmentId, final String senderFirstName, final String senderLastName,
			final String senderEmail, final String senderCity, final String senderStreet, final String senderPostalCode,
			final String recipientFirstName, final String recipientLastName, final String recipientEmail,
			final String recipientCity, final String recipientStreet, final String recipientPostalCode,
			final ShipmentSize shipmentSize, final String destination, final ShipmentStatus shipmentStatus,
			final ShipmentType shipmentType, final ShipmentId shipmentRelatedId, final LocalDateTime createdAt,
			final LocalDateTime updatedAt, final Boolean locked, final Country originCountry, final Country destinationCountry, 
            final Money price, final ShipmentPriority shipmentPriority) {
        this.shipmentId = shipmentId;
        this.firstName = senderFirstName;
        this.lastName = senderLastName;
        this.senderEmail = senderEmail;
        this.senderCity = senderCity;
        this.senderStreet = senderStreet;
        this.senderPostalCode = senderPostalCode;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.recipientEmail = recipientEmail;
        this.recipientCity = recipientCity;
        this.recipientStreet = recipientStreet;
        this.recipientPostalCode = recipientPostalCode;
        this.shipmentSize = shipmentSize;
        this.destination = destination;
        this.shipmentStatus = shipmentStatus;
        this.shipmentType = shipmentType;
        this.shipmentRelatedId = shipmentRelatedId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.locked = locked;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.dangerousGood = null;
        this.price = null;
        this.shipmentPriority = shipmentPriority;
        this.price = price;
    }

    public static ShipmentEntity from(final Shipment shipment) {
        final String senderFirstName = shipment.getSender().getFirstName();
        final String senderLastName = shipment.getSender().getLastName();
        final String senderEmail = shipment.getSender().getEmail();
        final String senderCity = shipment.getSender().getCity();
        final String senderStreet = shipment.getSender().getStreet();
        final String senderPostalCode = shipment.getSender().getPostalCode();
        final String recipientFirstName = shipment.getRecipient().getFirstName();
        final String recipientLastName = shipment.getRecipient().getLastName();
        final String recipientEmail = shipment.getRecipient().getEmail();
        final String recipientCity = shipment.getRecipient().getCity();
        final String recipientStreet = shipment.getRecipient().getStreet();
        final String recipientPostalCode = shipment.getRecipient().getPostalCode();
        return new ShipmentEntity(shipment.getShipmentId(), senderFirstName, senderLastName,
                senderEmail, senderCity, senderStreet, senderPostalCode,
                recipientFirstName, recipientLastName, recipientEmail, recipientCity, recipientStreet,
                recipientPostalCode, shipment.getShipmentSize(), shipment.getDestination(), 
                shipment.getShipmentStatus(), shipment.getShipmentType(), shipment.getShipmentRelatedId(),
                shipment.getCreatedAt(), shipment.getUpdatedAt(), shipment.isLocked(),
				shipment.getOriginCountry(), shipment.getDestinationCountry(), shipment.getPrice(),
				shipment.getShipmentPriority());
    }
}
