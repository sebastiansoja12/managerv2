package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ExternalId;
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
@Audited
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
    @Enumerated(EnumType.STRING)
    private ShipmentSize shipmentSize;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
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
    private CountryCode originCountry;

    @Column(name = "destination_country", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode destinationCountry;

    @Column(name = "shipment_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentPriority shipmentPriority;

    @OneToOne
    @JoinColumn(name = "dangerous_good_id", referencedColumnName = "dangerous_good_id")
    private DangerousGoodEntity dangerousGood;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @OneToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "shipment_id", insertable = false)
    private SignatureEntity signature;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "external_route_id"))
    private ExternalId<String> externalRouteId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "external_return_id"))
    private ExternalId<Long> externalReturnId;

    @Column(name = "external_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "external_id"))
    private ExternalId<String> externalId;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

	public ShipmentEntity(final ShipmentId shipmentId, final String senderFirstName, final String senderLastName,
			final String senderEmail, final String senderCity, final String senderStreet, final String senderPostalCode,
			final String senderTelephone, final String recipientFirstName, final String recipientLastName,
			final String recipientEmail, final String recipientCity, final String recipientStreet,
			final String recipientPostalCode, final String recipientTelephone, final ShipmentSize shipmentSize,
			final String destination, final ShipmentStatus shipmentStatus, final ShipmentType shipmentType,
			final ShipmentId shipmentRelatedId, final LocalDateTime createdAt, final LocalDateTime updatedAt,
			final Boolean locked, final CountryCode originCountry, final CountryCode destinationCountry,
			final Money price, final ShipmentPriority shipmentPriority, final DangerousGoodEntity dangerousGood,
			final ExternalId<String> externalRouteId, final ExternalId<Long> externalReturnId,
            final ExternalId<String> externalId, final String trackingNumber) {
        this.shipmentId = shipmentId;
        this.firstName = senderFirstName;
        this.lastName = senderLastName;
        this.senderEmail = senderEmail;
        this.senderCity = senderCity;
        this.senderStreet = senderStreet;
        this.senderPostalCode = senderPostalCode;
        this.senderTelephone = senderTelephone;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.recipientEmail = recipientEmail;
        this.recipientCity = recipientCity;
        this.recipientStreet = recipientStreet;
        this.recipientPostalCode = recipientPostalCode;
        this.recipientTelephone = recipientTelephone;
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
        this.shipmentPriority = shipmentPriority;
        this.price = price;
        this.dangerousGood = dangerousGood;
        this.externalRouteId = externalRouteId;
        this.externalReturnId = externalReturnId;
        this.externalId = externalId;
        this.trackingNumber = trackingNumber;
    }

    public static ShipmentEntity from(final Shipment shipment) {
        final String senderFirstName = shipment.getSender().getFirstName();
        final String senderLastName = shipment.getSender().getLastName();
        final String senderEmail = shipment.getSender().getEmail();
        final String senderCity = shipment.getSender().getCity();
        final String senderStreet = shipment.getSender().getStreet();
        final String senderPostalCode = shipment.getSender().getPostalCode();
        final String senderTelephoneNumber = shipment.getSender().getTelephoneNumber();
        final String recipientFirstName = shipment.getRecipient().getFirstName();
        final String recipientLastName = shipment.getRecipient().getLastName();
        final String recipientEmail = shipment.getRecipient().getEmail();
        final String recipientCity = shipment.getRecipient().getCity();
        final String recipientStreet = shipment.getRecipient().getStreet();
        final String recipientPostalCode = shipment.getRecipient().getPostalCode();
        final String recipientTelephoneNumber = shipment.getRecipient().getTelephoneNumber();
        final DangerousGoodEntity dangerousGoodEntity = DangerousGoodEntity.from(shipment.getDangerousGood());
        return new ShipmentEntity(shipment.getShipmentId(), senderFirstName, senderLastName,
                senderEmail, senderCity, senderStreet, senderPostalCode, senderTelephoneNumber,
                recipientFirstName, recipientLastName, recipientEmail, recipientCity, recipientStreet,
                recipientPostalCode, recipientTelephoneNumber, shipment.getShipmentSize(), shipment.getDestination(),
                shipment.getShipmentStatus(), shipment.getShipmentType(), shipment.getShipmentRelatedId(),
                shipment.getCreatedAt(), shipment.getUpdatedAt(), shipment.isLocked(),
				shipment.getOriginCountry(), shipment.getDestinationCountry(), shipment.getPrice(),
				shipment.getShipmentPriority(), dangerousGoodEntity, shipment.getExternalRouteId(),
                shipment.getExternalReturnId(), new ExternalId<>(shipment.getExternalShipmentId().value().toString()),
                shipment.getTrackingNumber().value());
    }
}
